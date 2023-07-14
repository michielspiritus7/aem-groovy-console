println "Example for checking permissions"

aecu
    .validateAccessRights()
    .forPaths("/content/we-retail/us/en/men", "/content/we-retail/de", "/content/we-retail/fr")
    .forGroups("administrators")
    .canRead()
    .canModify()
    .canCreate()
    .canDelete()
    .canReadAcl()
    .canWriteAcl()
    .canReadPage()
    .canDeletePage()
    .validate()
    
println "Multiple paths and groups can be tested in one run"


aecu
    .validateAccessRights()
    .forPaths("/content/we-retail/us/en/men", "/content/we-retail/de", "/content/we-retail/fr")
    .forGroups("content-authors")
    .canRead()
    .canModify()
    .canCreate()
    .canDelete()
    .canReplicate()
    .cannotReadAcl()
    .cannotWriteAcl()
    .canReadPage()
    .canModifyPage()
    .canDeletePage()
    .forPaths("/content/we-retail/us/en/products")
    .canCreatePage("/conf/we-retail/settings/wcm/templates/content-page")
    .cannotCreatePage("/conf/we-retail/settings/wcm/templates/nonexisting-page")
    .forGroups("contributor")
    .canRead()
    .cannotModify()
    .cannotCreate()
    .cannotDelete()
    .cannotReplicate()
    .cannotReadAcl()
    .cannotWriteAcl()
    .canReadPage()
    .cannotModifyPage()
    .cannotDeletePage()
    .validate()
    
println "Real replication will be tested only with simulate()"
    
    aecu
    .validateAccessRights()
    .forPaths("/content/we-retail/us/en/men")
    .forGroups("content-authors")
    .canReplicatePage(ReplicationActionType.ACTIVATE)
//    .simulate()
    .validate();