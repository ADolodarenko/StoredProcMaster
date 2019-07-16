set @echo off

set lp=%CD%\log

set dt=%DATE%
set tm=%TIME%

set yy=%dt:~-4%

set mm=%dt:~3,2%
set mm=%mm: =0%

set dd=%dt:~0,2%
set dd=%dd: =0%

set hh=%tm:~0,2%
set hh=%hh: =0%

set mn=%tm:~3,2%
set mn=%mn: =0%

rem set ds=%dd%.%mm%.%yy% %hh%:%mn%
set dt=%yy%%mm%%dd%

isql -Udb_task -Pdb_task -SDBSRV -ispm_add_new_proc.sql >> %lp%\%dt%_spm_service.txt
isql -Udb_task -Pdb_task -SDBSRV -ispm_update_proc_refs.sql >> %lp%\%dt%_spm_service.txt
rem SendEmailConsole.exe it-support@ksdbook.ru "alexey.dolodarenko@ksdbook.ru" "%lp%\%dt%_spm_update_proc_refs.txt" "StoredProcMaster services - log %ds%"
