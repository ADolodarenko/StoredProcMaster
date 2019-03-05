--�������� ��
if (object_id('spm_proc_info') is not null) drop table spm_proc_info
go
create table spm_proc_info(
proc_id int not null,
proc_description varchar(256) not null,
constraint pk_spm_proc_info primary key clustered (proc_id))
go
alter table spm_proc_info
    add constraint fk_spm_proc_info_proc foreign key (proc_id)
    references dbo.sysobjects (id)
go
--����� �� ��
if (object_id('spm_login_proc') is not null) drop table spm_login_proc
go
create table spm_login_proc(
login_id int not null,
proc_id int not null,
constraint pk_login_proc primary key clustered (login_id, proc_id))
go
alter table spm_login_proc
    add constraint fk_spm_login_proc_proc foreign key (proc_id)
    references dbo.sysobjects (id)
go
alter table spm_login_proc
    add constraint fk_spm_login_proc_login foreign key (login_id)
    references master..syslogins (suid)
go
--������� �� - ����� ��?
if (object_id('spm_proc_status') is not null) drop table spm_proc_status
go
create table spm_proc_status(
status_id int not null,
status_name varchar(255) not null,
constraint pk_spm_proc_status primary key clustered (status_id))
go
--�������� ��������� ������������ ��
if (object_id('spm_get_available_proc') is not null) drop proc spm_get_available_proc
go
create proc spm_get_available_proc(@login_name varchar(255))
as
begin

	select inf.proc_id, obj.name as proc_name, inf.proc_description
	from master..syslogins lg, spm_login_proc crs, sysobjects obj, spm_proc_info inf
	where lg.name = @login_name
	  and lg.suid = crs.login_id
	  and crs.proc_id = obj.id
	  and obj.type = 'P'
	  and obj.id = inf.proc_id
	
end
go
--������/������� ���� �� �� ����������� ������
if (object_id('spm_set_proc_for_login') is not null) drop proc spm_set_proc_for_login
go
create proc spm_set_proc_for_login(@login_name varchar(255), @proc_id int, @action_type int)
as
declare @login_id int, @access_granted int
begin

	select @login_id = suid from master..syslogins where name = @login_name
	
	if (@login_id is null)
	begin
		print 'Unknown login was specified.'
		
		return
	end
	
	if (not exists(select 1 from sysobjects where type = 'P' and id = @proc_id))
	begin
		print 'Unknown stored procedure was specified.'
		
		return
	end
	
	if (@action_type not in (1, 0))
	begin
		print 'Unknown action type was specified.'
		
		return
	end
	
	set @access_granted = case when exists (select 1 from spm_login_proc where proc_id = @proc_id and login_id = @login_id) then 1 else 0 end
		
	if (@action_type = 1)
	begin
		
		if (@access_granted = 1)
		begin
			print 'There is nothing to do.'
		
			return
		end
		
		insert spm_login_proc(login_id, proc_id) select @login_id, @proc_id
		
		print 'Access granted.'
				
	end
	else
	begin
		
		if (@access_granted = 0)
		begin
			print 'There is nothing to do.'
		
			return
		end
		
		delete spm_login_proc where login_id = @login_id and proc_id = @proc_id
		
		print 'Access revoked.'
		
	end

end
go
--����������� ������ �������� ��
if (object_id('spm_get_proc_text') is not null) drop proc spm_get_proc_text
go
create proc spm_get_proc_text(@proc_id int)
as
begin

	select text
	from syscomments
	where id = @proc_id
	
end
go

/*
delete spm_proc_info
go
insert spm_proc_info(proc_id, proc_description) select id, name from sysobjects where type = 'P'
go
spm_set_proc_for_login 'dolodarenko', 179679502, 1
--
spm_get_available_proc 'dolodarenko'
--
update spm_proc_info
set proc_description = 'StoredProcMaster: ��������� ���������� ������ �� ��'
where proc_id = 179679502
*/
