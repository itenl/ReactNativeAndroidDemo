import React from 'react';
import {
  StyleSheet,
  Text,
  View,
  NativeModules,
  TouchableNativeFeedback,
  Button,
  ScrollView,
  ToastAndroid
} from 'react-native';

class GetAllNativeModules extends React.Component {
  constructor(props) {
    super(props);
  }
  getAllNativeModules() {
    const nativeModules = NativeModules;
    let text = [];
    Object.keys(nativeModules).map((key, index, items) => {
      let val=nativeModules[key];
      text.push(
        <View key={key}>
          <Text >{key}</Text>
          <Text >{JSON.stringify(val)}</Text>
          <Text >====================================</Text>
        </View>
      );
    });
    return text;
  }

  render() {
    return (
      <ScrollView style={styles.container}>
        {/* <Text style={styles.hello}>GetAllNativeModules 得到 NativeModules 所有模块</Text> */}
        <Text style={styles.hello}>{this.props.COMPONENT_TYPE}</Text>
        {this.getAllNativeModules()}
      </ScrollView>
    );
  }
}
var styles = StyleSheet.create({
  container: {
    flex: 1
    // justifyContent: 'center'
  },
  hello: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10
  }
});

export default GetAllNativeModules;
