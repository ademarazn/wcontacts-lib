[![License: MIT](https://img.shields.io/github/license/ademarazn/wcontacts-lib.svg)](https://opensource.org/licenses/MIT)
[![JitPack](https://jitpack.io/v/ademarazn/wcontacts-lib.svg)](https://jitpack.io/#ademarazn/wcontacts-lib)

# WContacts Lib
A library to retrieve WhatsApp contacts stored on the device.

# How to
To use the WContacts Lib in your android project, just follow the steps below:

**Step 1.** Add the JitPack repository to your root build.gradle at the end of the repositories:
```Groovy
allprojects {
    repositories {
       ...
       maven { url 'https://jitpack.io' }
    }
}
```

**Step 2.** Add the dependency
```Groovy
dependencies {
    implementation 'com.github.ademarazn:wcontacts-lib:1.1'
}
```

## Usage
To retrieve the wContacts (WhatsApp contact objects), use ```WContactsLibrary.getWContacts(Activity activity, WContactsListener listener)``` method.

*NOTE: Read contacts permission must be granted by the user. The ```Permissions.requestReadContacts(Activity act, int code)``` method can be used to prompt the user for the request.*

*You may call ```getWContacts()``` again when ```onRequestPermissionsResult()``` is called. See [Sample](../master/sample/src/main/java/com/ademarazn/wcontacts/MainActivity.java).*

**Exemple:**
```Java
WContactsLibrary.getWContacts(activity, new WContactsListener() {
    @Override
    public void onSuccess(@NonNull List<WContact> wContacts) {
        for (WContact wContact : wContacts) {
            System.out.println(wContact);
        }
    }
    
    @Override
    public void onFailure(@NonNull Exception exception) {
        exception.printStackTrace();
        Permissions.requestReadContacts(activity, 101);
    }
});
```
This lib allows you to send, from any WContact retrieved, intents in which:
* opens WContact in contacts list:
```Java
WContactUtils.openContact(activity, wContact);
```
* starts conversation:
```Java
WContactUtils.startConversation(activity, wContact);
```
* makes voice call:
```Java
WContactUtils.makeVoiceCall(activity, wContact);
```
* makes video call:
```Java
WContactUtils.makeVideoCall(activity, wContact);
```

# License
Licensed under the MIT License. 

https://opensource.org/licenses/MIT

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
