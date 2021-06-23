# demo-build-sbt-devops-azure
test devops azure
https://bizone.atlassian.net/wiki/spaces/VELOCITY/pages/682852432/Investigate+SBT

Devops CI/CD Pipeline on Azure
1. Create one Pipeline<br/>
    1.1 get source from repository Git\
    1.2 config azure_pipeline Yaml\
        trigger:\
         - branch: access repository git to get source\
        pool\
         - vmImage: OS \
        steps\
         - script: command line to compile \
         - displayName: description execute command line\
   

Link referent:\
Key concepts overview \
https://docs.microsoft.com/en-us/azure/devops/pipelines/get-started/key-pipelines-concepts?view=azure-devops \
Explain Trigger:\
-  schedule defined using cron syntax
https://docs.microsoft.com/en-us/azure/devops/pipelines/process/scheduled-triggers?view=azure-devops&tabs=yaml \

https://docs.microsoft.com/en-us/azure/devops/pipelines/yaml-schema?view=azure-devops&tabs=example%2Cparameter-schema#step-templates

https://academy.databricks.com/course/apache-spark-programming-with-databricks
https://docs.microsoft.com/en-us/azure/databricks/notebooks/azure-devops-services-version-control

#- powershell: |
#    $destinationFolder = $HOME+"\.sbt"
#    if (!(Test-Path -path $destinationFolder)) {New-Item $destinationFolder -Type Directory}
#    New-Item $HOME/.sbt/.credentials -ItemType file
#    Add-Content  $HOME/.sbt/.credentials "`nrealm="
#    Add-Content  $HOME/.sbt/.credentials "`nhost=bizonedev.pkgs.visualstudio.com"
#    Add-Content  $HOME/.sbt/.credentials "`nuser=BizOneDev"
#    Add-Content  $HOME/.sbt/.credentials "`npassword="


https://spark.apache.org/docs/3.1.2/sql-data-sources-load-save-functions.html

sbt "release release-version 1.2.3 next-version 1.3.0 with-defaults"

https://stackoverflow.com/questions/63364782/azure-yaml-pipeline-file-strategy-in-branching