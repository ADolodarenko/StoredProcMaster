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

set ds=%yy%%mm%%dd%_%hh%-%mn%

isql -Udb_task -Pdb_task -SDBSRV -ispm_update_proc_refs.sql  -o%lp%\%ds%_spm_update_proc_refs.txt
SendEmailConsole.exe it-support@ksdbook.ru "alexey.dolodarenko@ksdbook.ru" "%lp%\%ds%_spm_update_proc_refs.txt" "StoredProcMaster services - log %dd%.%mm%.%yy% %hh%:%mn%"
