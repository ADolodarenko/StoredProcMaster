--Описание ХП
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
--Права на ХП
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
--Статусы ХП - нужно ли?
if (object_id('spm_proc_status') is not null) drop table spm_proc_status
go
create table spm_proc_status(
status_id int not null,
status_name varchar(255) not null,
constraint pk_spm_proc_status primary key clustered (status_id))
go
--Загрузка доступных пользователю ХП
if (object_id('spm_get_available_proc') is not null) drop proc spm_get_available_proc
go
create proc spm_get_available_proc(@login_name varchar(255))
as
begin

	/*
	*	Project: StoredProcMaster
	*	Description: Getting the list of procedures that are available to the specified login.
	*	Author: Dolodarenko A.V., 06.03.2019
	*/

	select inf.proc_id, obj.name as proc_name, inf.proc_description,
		1 as proc_status_id, convert(int, null) as occupant_id,
		convert(varchar(255), null) as occupant_login,
		convert(varchar(255), null) as occupant_name
	into #pl
	from master..syslogins lg, spm_login_proc crs, sysobjects obj, spm_proc_info inf 
	where lg.name = @login_name
	  and lg.suid = crs.login_id
	  and crs.proc_id = obj.id
	  and obj.type = 'P'
	  and obj.id = inf.proc_id
	  
	update #pl
	set proc_status_id = 2,
		occupant_id = sp.suid,
		occupant_login = suser_name(sp.suid),
		occupant_name = ul.user_name
	from master..sysprocesses sp, user_list ul
	where #pl.proc_id = sp.id
	  and sp.suid *= ul.user_id
	
	
	select proc_id, proc_name, proc_description, proc_status_id,
			occupant_id, occupant_login, occupant_name
	from #pl
	
end
go
--Выдача/изъятие прав на ХП конкретному логину
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
--Отображение текста заданной ХП
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
delete from spm_proc_info
go
insert spm_proc_info(proc_id, proc_description)
select so.id, so.name
from sysobjects so
where so.type = 'P'
  and not exists (select 1 from spm_proc_info where proc_id = so.id)
go
spm_set_proc_for_login 'dolodarenko', 1978689961, 1
--
spm_get_available_proc 'dolodarenko'
--
update spm_proc_info
set proc_description = 'Обновление good stock'
where proc_id = 332859796
--
insert spm_proc_status (status_id, status_name) select 1, 'Available'
insert spm_proc_status (status_id, status_name) select 2, 'Occupied'
*/
