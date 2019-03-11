use O
go

--Описание ХП
if (object_id('spm_proc_info') is not null) drop table spm_proc_info
go
create table spm_proc_info(
database_id smallint not null,
proc_id int not null,
proc_description varchar(256) not null,
constraint pk_spm_proc_info primary key clustered (database_id, proc_id))
go
alter table spm_proc_info
    add constraint fk_spm_proc_info_db foreign key (database_id)
    references master..sysdatabases (dbid)
go
create index idx_spm_proc_info_db on spm_proc_info(database_id)
create index idx_spm_proc_info_proc on spm_proc_info(proc_id)
go

--Права на ХП
if (object_id('spm_login_proc') is not null) drop table spm_login_proc
go
create table spm_login_proc(
login_id int not null,
database_id smallint not null,
proc_id int not null,
constraint pk_login_proc primary key clustered (login_id, database_id, proc_id))
go
alter table spm_login_proc
    add constraint fk_spm_login_proc_login foreign key (login_id)
    references master..syslogins (suid)
go
alter table spm_login_proc
    add constraint fk_spm_login_proc_db foreign key (database_id)
    references master..sysdatabases (dbid)
go
create index idx_spm_login_proc_login on spm_login_proc(login_id)
create index idx_spm_login_proc_db on spm_login_proc(database_id)
create index idx_spm_login_proc_proc on spm_login_proc(proc_id)
go

--Статусы ХП - нужно ли?
if (object_id('spm_proc_status') is not null) drop table spm_proc_status
go
create table spm_proc_status(
status_id int not null,
status_name varchar(255) not null,
constraint pk_spm_proc_status primary key clustered (status_id))
go

/*
insert spm_proc_status (status_id, status_name) select status_id, status_name from FLC_RU..spm_proc_status
--
insert spm_proc_info (database_id, proc_id, proc_description)
select 5, x.proc_id, x.proc_description
from FLC_RU..spm_proc_info x, FLC_RU..sysobjects y
where x.proc_id = y.id
  and y.type = 'P'
--
insert spm_login_proc (login_id, database_id, proc_id)
select z.login_id, 5, z.proc_id
from FLC_RU..spm_login_proc z, FLC_RU..spm_proc_info x, FLC_RU..sysobjects y
where z.proc_id = x.proc_id 
  and x.proc_id = y.id
  and y.type = 'P'  
*/
--Загрузка доступных пользователю ХП
if (object_id('spm_get_available_proc') is not null) drop proc spm_get_available_proc
go
create proc spm_get_available_proc(@login_name varchar(255), @db_name varchar(255))
as
declare @login_id int, @db_id smallint, @cmd_text varchar(2048)
begin

	---------------------------------------
	--	Project: StoredProcMaster
	--	Description: Getting the list of procedures that are available to the specified login at the specified database.
	--	Author: Dolodarenko A.V., 11.03.2019
	---------------------------------------

	--Checking input parameters
	select @login_id = suid from master..syslogins where name = @login_name
	
	if (@login_id is null)
	begin
		
		return
		
	end
	
	select @db_id = dbid from master..sysdatabases where name = @db_name
	
	if (@db_id is null)
	begin
		
		return
		
	end
	
	create table #pl(proc_id int not null,
					proc_name varchar(255) not null,
					proc_description varchar(255) null,
					proc_status_id int not null,
					owner_id int not null,
					owner_login varchar(255) not null,
					owner_name varchar(255) null,
					occupant_id int null,
					occupant_login varchar(255) null,
					occupant_name varchar(255) null)
					
	create index XX001 on #pl(proc_id)
	
	set @cmd_text = 'insert #pl (proc_id, proc_name, proc_description, proc_status_id, owner_id, owner_login, owner_name) ' + 
					'select inf.proc_id, obj.name, inf.proc_description, ' + 
					'1, ' + 
					'su.suid, ' + 
					'suser_name(su.suid), ' + 
					'ul.user_name ' + 
					'from spm_login_proc crs, ' + @db_name + '..sysobjects obj, ' + @db_name + '..sysusers su, spm_proc_info inf, FLC_RU..user_list ul ' + 
					'where crs.login_id = @login_id ' + 
					'  and crs.database_id = @db_id ' + 
					'  and crs.proc_id = obj.id ' + 
					'  and obj.id = inf.proc_id ' + 
					'  and obj.uid *= su.uid' + 
					'  and su.suid *= ul.user_id'
	
					
	exec (@cmd_text)
	
	if exists (select 1 from #pl)
	begin
		
		update #pl
		set proc_status_id = 2,
			occupant_id = sp.suid,
			occupant_login = suser_name(sp.suid),
			occupant_name = ul.user_name
		from master..sysprocesses sp, FLC_RU..user_list ul
		where #pl.proc_id = sp.id
		  and sp.suid *= ul.user_id
		
	end
	
	
	select proc_id, proc_name, proc_description, proc_status_id,
			owner_id, owner_login, owner_name,
			occupant_id, occupant_login, occupant_name
	from #pl
	
end
go