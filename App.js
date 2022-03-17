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

// import { RNDataUsageLibrary } from 'react-native-data-usage-library';
// import RNDatausagecheck from 'react-native-datausagecheck';
// import CallLogs from 'react-native-call-log';

export default function App() {
  useEffect(()=>{
    const { NetworkManager } = NativeModules;
    console.error(Object.getOwnPropertyNames( NativeModules ));
  })
  return (
    <SafeAreaView style={styles.container}>
      <View>
        <Text style={styles.titleText}>
          How to Access Call Logs of Android Devices
          from React Native App
        </Text>
        {/* <FlatList
          data={listData}
          //data defined in constructor
          ItemSeparatorComponent={ItemSeparatorView}
          //Item Separator View
          renderItem={ItemView}
          keyExtractor={(item, index) => index.toString()}
        /> */}
      </View>
    </SafeAreaView>
  );
}



const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'white',
    padding: 10,
  },
  titleText: {
    fontSize: 22,
    textAlign: 'center',
    fontWeight: 'bold',
  },
  textStyle: {
    fontSize: 16,
    marginVertical: 10,
  },
});

