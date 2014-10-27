mendix-ObjectBackupRestore
==========================

Backup and restore of Mendix objects

##Description
Create a backup of one or more Mendix objects, including referenced objects and file document contents. Backup configurations are used to specify which referenced objects will be included in the backup.


##Disclaimer
- Please note that the ObjectBackupRestore module is ***not*** intended to replace a full database or cloud backup plan.
- The functionality is provided without official support.
- Support is provided through the Mendix forum on best-effort basis only.
- We do not accept responsibility for loss or corruption of data.
- Use at your own risk.

**By using this module you agree to be bound by this disclaimer.**  

##Typical usage scenario
- Restore objects of one organization in a multi-tenant environment.
- Duplicate a part of the data to another environment. (Note that you can also duplicate backup configurations this way.)

###Multi-tenant environment
When one customer organization in a multi-tenant environment request a restore of their data, the daily backup cannot be used as that would affect all customers in the node.

The following steps allow restoring data of one customer:

- Restore the full backup in another node or environment.
- In that node, use this module to create a backup of the data of the customer.
- Restore that backup in the production environment.

##Features
- Backup any object in the model
- Use an XPath constraint to limit the objects included.
- Drill-down into referenced objects. For each reference can be specified whether the referenced objects must be included in the backup.
- FileDocument content is also included.
- The restore checks, for objects that have a functional key, whether the object already exists in the target environment. Existing objects will never be overwritten or changed.

##Limitations

###It is a normal Mendix module
The ObjectBackupRestore module works like any other Mendix application. Restoring an object using this module is the same as creating a new object with the same values.

####Attribute values supplied by the runtime

- Object IDs and AutoNumber attribute values are different after a restore
- Owner is set to the user that restored the backup.
 
If you need auditing of your data, do not rely on the default Owner and ChangedBy but implement this in your own attributes and associations. You could also use the Audittrail module, available in the appstore.

####Entity events  
Commit events are skipped, but there is currently no way to skip AfterCreate events, so the microflow that handles the event must be able to cope with this. A simple way is to use only MxAdmin to restore objects and have the microflow quit if the user is MxAdmin.

###Only one attribute as functional key
See below at Object configuration.

##Dependencies
- Mendix 5.9.1.
- MxModelReflection. Used on the configuration pages, not during backup and restore.
- CommunityCommons.
- Bootstrap TreeView widget (included).

##Installation
Normal appstore installation.
Link the pages in the UseMe folder to your navigation. Include the Admin module role in the Administrator user role.

##Configuration

###Reflection
Any module that contains entities or associations must be synchronized in Reflection. That includes System, Administration or even MxModelReflection itself if your entities reference entities in those modules.

###Backup group
A backup group allows backup configurations to be grouped.

###Backup configuration
Backups are created using backup configurations. A backup configuration specifies which entity is the main entity of the backup. It also specifies the default XPath constraint (optional) for backup requests that are based on this configuration.

After creating a new backup configuration, the edit backup configuration page is displayed. It shows the main entity and its associations. The associations are displayed in red because for each association must be decided whether to include the referenced entity in the backup. A backup can only be created when all references have include status Yes or No.

By clicking on an association, its details are displayed. The key of the referenced entity, if any, can be selected by clicking on the link.

####Include in backup: Yes
The referenced objects are also included in the backup. The association is expanded to show the next level. When there are multiple paths to an object, it is sufficient to expand the reference in one place. The backup and restore detect that the same object is referenced. An example is a multi-tenant model, where all entities are related to the customer organization entity.

**Do not include entites of System or Administration in your backups!** Just specify the functional key on the object type and the restore process will link your data to these entities.

(You can use the module to duplicate backup configurations.)

####Include in backup: No
The object only holds the ID and, optionally, the functional key of the referenced object.
A functional key is necessary when the referenced entity is not included in the backup, to allow the restore to link the restored object to the existing object. 

####Specializations
Take care when an entity has specializations. Each specialization must be treated as a separate part of the configuration. The backup does not take the generalization into account when processing a specialization. 

###Backup request
A backup request is necessary to create a backup. It refers to the backup configuration to use and allows for a specific XPath constraint.

###Create a backup
From the backup request screen, a backup can be created by selecting a backup request and clicking the *Run backup* button. Please make sure that noone is changing the objects you want to backup.

###Working with backups
The Backups page shows a list of backups.

####Show backup details and download the backup file
Click on the *Information* button or double click an existing backup to show details and download the backup file.

####Import a backup from another environment
Import a backup file using the *Import* button. The backup file will be checked and then added to the list.

###Restore a backup
A backup can be restored from the Backups page. Select a backup and click *Restore*. A page is shown with some details and counts of the backup. If desired, the objects in the backup can be displayed by clicking *Show objects*. The restore can be started by clicking *Restore*.

###Restore requests
Each time a backup was selected for restore, a restore request is created. Each restore request has a log, which shows the number of objects restored after a succesful restore. If the restore was not succesful, the log will contain errormessages.  

###Object configuration: functional key
For each entity included in the backup, a functional key can be specified. This functional key is only used during the restore to check whether the object already exists. Objects without a functional key are always restored.

The functional key of an entity can consist of one attribute only, **the value must be unique** for the entire entity. It is strongly advised to enforce unique values using a unique rule on the attribute in the domain model. 

Only Enumeration, Integer, Long and String attributes are supported. (AutoNumber is not possible because a new value is generated during the restore.)  

As commit events are skipped, a before commit event can be used to generate a unique key if necessary.

###Logging
The ObjectBackupRestore module logs to log node *ObjectBackupRestore*. Set this log node to debug level for detailed information about each object processed in a backup or restore. Trace level includes even more detail, for problem solving.