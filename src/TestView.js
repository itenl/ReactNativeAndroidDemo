import React from 'react';
import {
  StyleSheet,
  Text,
  View,
  NativeModules,
  TouchableNativeFeedback,
  Button,
  ToastAndroid
} from 'react-native';

class TestView extends React.Component {
  render() {
    return (
      <View style={styles.container}>
        <Button
          title={'跳转到原生页面'}
          onPress={() => {
            NativeModules.MyIntentModule.startActivityFromJS(
              'com.example.cmacking.reactnativeandroiddemo.WelcomeActivity',
              '我是RN TestView 的页面数据'
            );
          }}
        />
        <Text style={styles.hello}>TestView 这里是RN页面</Text>
      </View>
    );
  }
}
var styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center'
  },
  hello: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10
  }
});

export default TestView;
