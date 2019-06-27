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
alter table spm_proc_info
add proc_name varchar(40) default '' not null
go
create index idx_spm_proc_info_name on spm_proc_info(proc_name)
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


update spm_proc_info
set proc_name = so.name
from spm_proc_info i, FLC_RU..sysobjects so
where i.database_id = 5
  and i.proc_id = so.id
  and so.type = 'P'
--(2406 rows affected)

select * from spm_proc_info where proc_name = ''

update spm_proc_info
set proc_name = proc_description
where database_id = 5
  and proc_name = ''
go




/*
insert spm_proc_status (status_id, status_name) select status_id, status_name from FLC_RU..spm_proc_status
--
delete spm_proc_info

select * from FLC_RU..sysusers

insert spm_proc_info (database_id, proc_id, proc_description)
select 5, x.proc_id, x.proc_description
from FLC_RU..spm_proc_info x, FLC_RU..sysobjects y
where x.proc_id = y.id
  and y.type = 'P'
  and y.uid = 1
  
--(2305 rows affected)

delete spm_login_proc

insert spm_login_proc (login_id, database_id, proc_id)
select z.login_id, 5, z.proc_id
from FLC_RU..spm_login_proc z, FLC_RU..spm_proc_info x, FLC_RU..sysobjects y
where z.proc_id = x.proc_id 
  and x.proc_id = y.id
  and y.type = 'P' 
  and y.uid = 1 
  
(2305 rows affected)

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
	--	Author: Dolodarenko A.V., 12.03.2019
	---------------------------------------

	--Checking input parameters
	select @login_id = suid from master..syslogins where name = @login_name
	
	if (@login_id is null)
	begin
		
		print 'Couldn''t find login ''%1!''', @login_name
		
		return
		
	end
	
	select @db_id = dbid from master..sysdatabases where name = @db_name
	
	if (@db_id is null)
	begin
		
		print 'Couldn''t find database ''%1!''', @db_name
		
		return
		
	end
	
	--Obtain data
	create table #pl(proc_id int not null,
					proc_name varchar(255) not null,
					proc_description varchar(255) null,
					proc_status_id int not null,
					occupant_id int null,
					occupant_login varchar(255) null,
					occupant_name varchar(255) null)
					
	create index XX001 on #pl(proc_id)
	
	set @cmd_text = 'insert #pl (proc_id, proc_name, proc_description, proc_status_id) ' + 
					'select inf.proc_id, obj.name, inf.proc_description, 1 ' + 
					'from spm_login_proc crs, ' + @db_name + '..sysobjects obj, spm_proc_info inf ' + 
					'where crs.login_id = @login_id ' + 
					'and crs.database_id = @db_id ' + 
					'and crs.proc_id = obj.id ' + 
					'and obj.type = ''P'' ' + 
					'and obj.id = inf.proc_id ' + 
					'and obj.uid = 1'
					
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
			occupant_id, occupant_login, occupant_name
	from #pl
	
end
go
--Выдача/изъятие прав на ХП конкретному логину
if (object_id('spm_set_proc_for_login') is not null) drop proc spm_set_proc_for_login
go
create proc spm_set_proc_for_login(@login_name varchar(255), @db_name varchar(255), @proc_name varchar(255), @action_type int)
as
declare @login_id int, @db_id smallint, @proc_id int, @access_granted int, @cmd_text varchar(2048)
begin

	---------------------------------------
	--	Project: StoredProcMaster
	--	Description: Setting access of the specified login to the specified procedure at the specified database.
	--	Author: Dolodarenko A.V., 12.03.2019
	---------------------------------------

	--Checking input parameters
	if (@action_type not in (1, 0))
	begin
		
		print 'Unknown action type was specified.'
		
		return
		
	end
	
	select @login_id = suid from master..syslogins where name = @login_name
	
	if (@login_id is null)
	begin
		
		print 'Couldn''t find login ''%1!''', @login_name
		
		return
		
	end
	
	select @db_id = dbid from master..sysdatabases where name = @db_name
	
	if (@db_id is null)
	begin
		
		print 'Couldn''t find database ''%1!''', @db_name
		
		return
		
	end
	
	set @cmd_text = 'select @proc_id = inf.proc_id ' + 
					'from ' + @db_name + '..sysobjects obj, spm_proc_info inf ' +
					'where obj.type = ''P'' ' + 
					'and obj.name = @proc_name ' + 
					'and obj.uid = 1 ' + 
					'and obj.id = inf.proc_id ' + 
					'and inf.database_id = @db_id'
					
	exec(@cmd_text)
		
	if (@proc_id is null)
	begin
	
		print 'Couldn''t find procedure ''%1!'' in database ''%2!''', @proc_name, @db_name
		
		return
		
	end
	
	set @access_granted = case when exists (select 1 from spm_login_proc where database_id = @db_id and proc_id = @proc_id and login_id = @login_id) then 1 else 0 end
		
	if (@action_type = 1)
	begin
		
		if (@access_granted = 1)
		begin
			
			print 'There is nothing to do.'
		
			return
			
		end
		
		insert spm_login_proc(login_id, database_id, proc_id) select @login_id, @db_id, @proc_id
		
		print 'Access granted.'
				
	end
	else
	begin
		
		if (@access_granted = 0)
		begin
			
			print 'There is nothing to do.'
		
			return
			
		end
		
		delete spm_login_proc where login_id = @login_id and database_id = @db_id and proc_id = @proc_id
		
		print 'Access revoked.'
		
	end

end
go
--Отображение текста заданной ХП
if (object_id('spm_get_proc_text') is not null) drop proc spm_get_proc_text
go
create proc spm_get_proc_text(@db_name varchar(255), @proc_id int)
as
declare @db_id smallint, @cmd_text varchar(2048)
begin

	---------------------------------------
	--	Project: StoredProcMaster
	--	Description: Getting the text of the specified procedure at the specified database.
	--	Author: Dolodarenko A.V., 12.03.2019
	---------------------------------------

	--Checking input parameters	
	select @db_id = dbid from master..sysdatabases where name = @db_name
	
	if (@db_id is null)
	begin
		
		print 'Couldn''t find database ''%1!''', @db_name
		
		return
		
	end
	
	--Getting the text
	set @cmd_text = 'select text ' + 
					'from ' + @db_name + '..syscomments ' +
					'where id = @proc_id'
					
	exec(@cmd_text)
	
end
go


--Обновление служебных таблиц
if (object_id('spm_update_proc_refs') is not null) drop proc spm_update_proc_refs
go
create proc spm_update_proc_refs(@db_name varchar(255), @proc_name varchar(255))
as
begin
	
	select @db_id = dbid from master..sysdatabases where name = @db_name
	
	if (@db_id is null)
	begin
		
		print 'Couldn''t find database ''%1!''', @db_name
		
		return
		
	end
	
	set @cmd_text = 'select @proc_id = inf.proc_id ' + 
					'from ' + @db_name + '..sysobjects obj, spm_proc_info inf ' +
					'where obj.type = ''P'' ' + 
					'and obj.name = @proc_name ' + 
					'and obj.uid = 1 ' + 
					'and obj.id = inf.proc_id ' + 
					'and inf.database_id = @db_id'
					
	exec(@cmd_text)
		
	if (@proc_id is null)
	begin
	
		print 'Couldn''t find procedure ''%1!'' in database ''%2!''', @proc_name, @db_name
		
		return
		
	end


end