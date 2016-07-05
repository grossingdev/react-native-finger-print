#FingerPrint react-native module for android.

## Install finger print module
   run npm git://github.com/nickhagenov/react-native-fingerprint.git#e9fc8600ef20ffbe1fa7310f0c61c59148c16e63 --save

## Configure Android Project
### In `android/setting.gradle`

    ```
    ...
    include ':app'
    include ':react-native-fingerprint'
    project(':react-native-fingerprint').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-fingerprint/android')
    ```

### In `android/app/build.gradle`

    ```
    ...
    dependencies {
        ...
        compile project(":react-native-fingerprint")
    }
    ```

### Register module (in MainActivity.java)

    ```
    import com.rn.fingerprint.FingerPrintPackage; // <--- import

    public class MainActivity extends ReactActivity {
      ......

      @Override
      protected List<ReactPackage> getPackages() {
          return Arrays.<ReactPackage>asList(
              new MainReactPackage(),
              new FingerPrintPackage() // <---- add here
          );
      }
      ......
    }
    ```
## Example