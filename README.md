## settings
- requires androidstudio
- JDK 1.8 works fine
- requires a device (smartphone) with Bluetooth capabilities (BLE 5)
## structure
### generic
- the code is more or less a three-tier architecture, with the app tier in java/, no data tier, and a presentation tier in res/
- Tools folder is for utilities, most of them are standard except PolicyEngine
### app layer
- BLEDeviceListAdapter is a class used to display ADPC notices in what is called a RecyclerView (dynamic presentation)
- Most of the code is in the MainActivity
    - everything starts in the function onCreate(), in which one can find the definitions of variables, the creation of buttons, the attribution of callback function for async methods, the preparation of buttons and various functions (such as defining a BLEScanner)
    - we define two button, one (fab) launches the scan of ble devices, the other button communicates consent
    - clicking on the fab button triggers startscanprepare() then startscan(), everything happens in the callback afterwards
    - the callback listens to BLE devices, and call a function to reconstitute broadcast data
    - PolicyEngine in Tools/ provides a toolkit to reconstitute ADPCnotice
    - a notice has to be parsed, the current set of purposes display is stored in a hashmap
    - once a notice has been fully recovered, the BLE service must be bound
    - when a user clicks on a toggle button, it modifies another hashmap (consents), later used to communicate the ID of purposes consented
### presentation layer
- the presentation is managed in res/, the code is in xml with a graphical equivalence, each acitivity (user interface) needs a codesheet in java/ and one in res/layout/
- MainActivity's sister codesheet is activity_main.xml, the way the content is presented is defined in content_main.xml
- content_main defines a recyclerview item, the content of which is presented in devices_list
- for each purpose of collection, the user is presented the notice (a TextView item whose id is device) and a button to consent or not (a Switch item whose id is switch1), the other is invisible and kept only for functioning purposes
## limitations/known issues
- code is not flexible in the sense that the ESP MAC address is hardcoded (should be retrieved in startScan() instead)
