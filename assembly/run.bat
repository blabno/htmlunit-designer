@echo off

SetLocal EnableDelayedExpansion

set CLASSPATH=.

for %%f in (lib\*.jar) do Set CLASSPATH=!CLASSPATH!;%%f

java -cp %CLASSPATH% pl.com.it_crowd.htmlunit_designer.MainFrame


