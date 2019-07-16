set nocount on
go
set proc_return_status off
go
use O
go 
declare @status int

exec @status = spm_add_new_proc

if (@status != 0)
	print '(return status = %1!)', @status
go
set proc_return_status on
go