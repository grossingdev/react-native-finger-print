#FingerPrint react-native module for android.

## Install finger print module
### Run npm command directly.
    npm install git://github.com/nickhagenov/react-native-fingerprint.git#(latest commit uuid) --save

### Add following line into package.json and run npm i (or npm install)
    "react-native-fingerprint": "git://github.com/nickhagenov/react-native-fingerprint.git#(latest commit uuid)"

## Configure Android Project
### In `android/setting.gradle`

    ...
    include ':app'
    include ':react-native-fingerprint'
    project(':react-native-fingerprint').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-fingerprint/android')

### In `android/app/build.gradle`

    ...
    dependencies {
        ...
        compile project(":react-native-fingerprint")
    }

### Register module (in MainActivity.java)

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

## Example
```
.....
import FingerPrint from 'react-native-fingerprint';
.....

class TestExample extends Component {
  constructor(props, context) {
    super(props, context);
    ......
    this.onFingerAuth = this.onFingerAuth.bind(this);
    ......
  }
  componentDidMount() {
    ......
    FingerPrint.registerFingerPrintCallback(this.onFingerAuth);
    FingerPrint.initializeFingerPrintAuth();
    ......
  }

  componentWillUnmount() {
    ......
    FingerPrint.removeFingerPrintCallback();
    ......
  }

  onFingerAuth(result) {
    if (result.success) {
        // add codes for success functionality
    } else {
      // add codes for failed functionality
    }
  }

  .....
}
......
```