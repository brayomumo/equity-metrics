import { StatusBar } from 'expo-status-bar';
import React, { useState, useEffect } from 'react';
import {
  SafeAreaView,
  Platform,
  StyleSheet,
  Text,
  View,
  PermissionsAndroid,
  FlatList,
  NativeModules
} from 'react-native';

import { RNDataUsageLibrary } from 'react-native-data-usage-library';
import CallLogs from 'react-native-call-log';

export default function App() {
  console.log(RNDataUsageLibrary)

  const dataModule = () => {
    if (NativeModules.DataUsageModule) {
      // Get data usage of all installed apps in current device
      // Parameters "startDate" and "endDate" are optional (works only with Android 6.0 or later). Declare empty object {} for no date filter.
      NativeModules.DataUsageModule.listDataUsageByApps({
        "startDate": new Date(2021, 4, 22, 0, 0, 0, 0).getTime(),
        "endDate": new Date().getTime()
      },
        (err, jsonArrayStr) => {
          if (!err) {
            var apps = JSON.parse(jsonArrayStr);
            console.log(apps);
            for (var i = 0; i < apps.length; i++) {
              var app = apps[i];
              console.log("App name: " + app.name + "\n"
                + "Package name: " + app.packageName + "\n"
                + "Received bytes: " + app.rx + "bytes\n"
                + "Transmitted bytes: " + app.tx + "bytes\n"
                + "Received MB: " + app.rxMb + "\n"
                + "Transmitted MB: " + app.txMb);
            }
          }
        })
      NativeModules.DataUsageModule.getDataUsageByApp({
        "packages": ["com.facebook.katana", "com.google.android.youtube", "com.whatsapp"],
        "startDate": new Date(2017, 4, 22, 0, 0, 0, 0).getTime(), // 1495422000000 = Mon May 22 2017 00:00:00
        "endDate": new Date().getTime()
      },
        (err, jsonArrayStr) => {
          if (!err) {
            var apps = JSON.parse(jsonArrayStr);
            console.log(apps);
            for (var i = 0; i < apps.length; i++) {
              var app = apps[i];
              console.log("App name: " + app.name + "\n"
                + "Package name: " + app.packageName + "\n"
                + "Received bytes: " + app.rx + "bytes\n"
                + "Transmitted bytes: " + app.tx + "bytes\n"
                + "Received MB: " + app.rxMb + "\n"
                + "Transmitted MB: " + app.txMb);
            }
          }
        });

      // Check if app has permission to access data usage by apps
      // This way will not ask for permissions (check only)
      // If you pass "requestPermission": "true", then app will ask for permissions.
      NativeModules.DataUsageModule.requestPermissions({ "requestPermission": "false" }, (err, result) => {
        var permissionObj = JSON.parse(result);
        if (!permissionObj.permissions) {
          Alert.alert('Give Permission',
            'You need to enable data usage access for this app. Please, enable it on the next screen.',
            [
              { text: 'Cancel', style: 'cancel', onPress: () => Actions.pop() },
              { text: 'Give permission', onPress: () => this.requestPermissions() }
            ],
            { cancelable: false });
        }
      });
    }

    return (
      <View style={styles.container}>
        <Text>Hello world</Text>
        {/* <Text> {dataUsageModule()} </Text> */}
        <StatusBar style="auto" />
      </View>
    );
  }

}

// export default function App() {
//   const [listData, setListDate] = useState([]);

//   useEffect(() => {
//     async function fetchData() {
//       if (Platform.OS != 'ios') {
//         try {
//           const granted = await PermissionsAndroid.request(
//             PermissionsAndroid.PERMISSIONS.READ_CALL_LOG,
//             {
//               title: 'Call Log Example',
//               message: 'Access your call logs',
//               buttonNeutral: 'Ask Me Later',
//               buttonNegative: 'Cancel',
//               buttonPositive: 'OK',
//             },
//           );
//           if (granted === PermissionsAndroid.RESULTS.GRANTED) {
//             CallLogs.loadAll().then((c) => setListDate(c));
//             CallLogs.load(3).then((c) => console.log(c));
//           } else {
//             alert('Call Log permission denied');
//           }useEffect
//         } catch (e) {
//           alert(e);
//         }
//       } else {
//         alert(
//           'Sorry! You cant get call logs in iOS devices because of the security concern',
//         );
//       }
//     }
//     fetchData();
//   }, []);

//   const ItemView = ({item}) => {
//     return (
//       // FlatList Item
//       <View>
//         <Text style={styles.textStyle}>
//           Name : {item.name ? item.name : 'NA'}
//           {'\n'}
//           DateTime : {item.dateTime}
//           {'\n'}
//           Duration : {item.duration}
//           {'\n'}
//           PhoneNumber : {item.phoneNumber}
//           {'\n'}
//           RawType : {item.rawType}
//           {'\n'}
//           Timestamp : {item.timestamp}
//           {'\n'}
//           Type : {item.type}
//         </Text>
//       </View>
//     );
//   };

//   const ItemSeparatorView = () => {
//     return (
//       // FlatList Item Separator
//       <View
//         style={{
//           height: 0.5,
//           width: '100%',
//           backgroundColor: '#C8C8C8',
//         }}
//       />
//     );
//   };


//   return (
//     <SafeAreaView style={styles.container}>
//       <View>
//         <Text style={styles.titleText}>
//           How to Access Call Logs of Android Devices
//           from React Native App
//         </Text>
//         <FlatList
//           data={listData}
//           //data defined in constructor
//           ItemSeparatorComponent={ItemSeparatorView}
//           //Item Separator View
//           renderItem={ItemView}
//           keyExtractor={(item, index) => index.toString()}
//         />
//       </View>
//     </SafeAreaView>
//   );
// };


// const styles = StyleSheet.create({
//   container: {
//     flex: 1,
//     backgroundColor: 'white',
//     padding: 10,
//   },
//   titleText: {
//     fontSize: 22,
//     textAlign: 'center',
//     fontWeight: 'bold',
//   },
//   textStyle: {
//     fontSize: 16,
//     marginVertical: 10,
//   },
// });
//   
// const styles = StyleSheet.create({
//   container: {
//     flex: 1,
//     backgroundColor: '#fff',
//     alignItems: 'center',
//     justifyContent: 'center',
//   },
// });
