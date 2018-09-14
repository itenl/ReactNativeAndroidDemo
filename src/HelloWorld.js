import React from 'react';
import {
  StyleSheet,
  Text,
  View,
  NativeModules,
  TouchableNativeFeedback,
  ToastAndroid
} from 'react-native';

class HelloWorld extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      test: '123456'
    };
  }

  // _onPressButton() {
  //   console.log('You tapped the _onPressButton!');
  //   NativeModules.MyIntentModule.startActivityFromJS(
  //     'com.example.cmacking.reactnativeandroiddemo.MyActivity',
  //     '我是RN HelloWorld 的页面数据'
  //   );
  // }

  // _onPressButtonByData() {
  //   console.log('You tapped the _onPressButtonByData!');
  //   NativeModules.MyIntentModule.dataToJS(
  //     msg => {
  //       console.log(msg);
  //       ToastAndroid.show(
  //         'JS界面:从Activity中传输过来的数据为:' + msg,
  //         ToastAndroid.SHORT
  //       );
  //     },
  //     result => {
  //       ToastAndroid.show('JS界面:错误信息为:' + result, ToastAndroid.SHORT);
  //     }
  //   );
  // }

  // _onPressButtonByNativeToast() {
  //   NativeModules.MyIntentModule.show(
  //     'Rn调用原生Toash方法及常量:' + NativeModules.MyIntentModule.OBJ,
  //     NativeModules.MyIntentModule.LONG
  //   );
  //   alert('JS INVOKE OVER');
  // }

  // async _onPressButtonByTestPromise() {
  //   try {
  //     var {
  //       relativeX,
  //       relativeY,
  //       width,
  //       height,
  //     } = await NativeModules.MyIntentModule.testPromise(10, 100);
  //     alert(relativeX + ':' + relativeY + ':' + width + ':' + height);
  //   } catch (e) {
  //     console.error(e);
  //   }
  // }

  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.hello}>Hello, World 这里是RN页面</Text>
        <Text style={styles.hello}>
          bundle:
          {this.props.bundle}
        </Text>
        <Text style={styles.hello}>
          msg:
          {this.props.msg &&
            JSON.parse(this.props.msg).test + JSON.parse(this.props.msg).str}
        </Text>
        <TouchableNativeFeedback
          onPress={() => {
            NativeModules.MyIntentModule.startActivityFromJS(
              'com.example.cmacking.reactnativeandroiddemo.MyActivity',
              '我是RN HelloWorld 的页面数据'
            );
          }}
        >
          <Text>跳转到原生页面 MyActivity</Text>
        </TouchableNativeFeedback>
        <TouchableNativeFeedback
          onPress={() => {
            NativeModules.MyIntentModule.startActivityFromJSGetResult(
              'com.example.cmacking.reactnativeandroiddemo.MyActivity',
              200,
              msg => {
                ToastAndroid.show(
                  'JS界面:从Activity中传输过来的数据为:' + msg,
                  ToastAndroid.SHORT
                );
              },
              result => {
                ToastAndroid.show(
                  'JS界面:错误信息为:' + result,
                  ToastAndroid.SHORT
                );
              }
            );
          }}
        >
          <Text>
            跳转到原生页面 MyActivity，支持在 MyActivity 跳转回来接收 Intent
            的传参
          </Text>
        </TouchableNativeFeedback>
        <TouchableNativeFeedback
          onPress={() => {
            NativeModules.MyIntentModule.dataToJS(
              msg => {
                console.log(msg);
                ToastAndroid.show(
                  'JS界面:从Activity中传输过来的数据为:' + msg,
                  ToastAndroid.SHORT
                );
              },
              result => {
                ToastAndroid.show(
                  'JS界面:错误信息为:' + result,
                  ToastAndroid.SHORT
                );
              }
            );
          }}
        >
          <Text>调用原生 dataToJS 获取原生数据执行回调</Text>
        </TouchableNativeFeedback>
        <TouchableNativeFeedback
          onPress={() => {
            NativeModules.MyIntentModule.show(
              'Rn调用原生Toash方法及常量:' + NativeModules.MyIntentModule.OBJ,
              NativeModules.MyIntentModule.LONG
            );
          }}
        >
          <Text>调用原生 show 方法及静态变量 TEST_OBJECT_KEY</Text>
        </TouchableNativeFeedback>
        <TouchableNativeFeedback
          onPress={async () => {
            try {
              var {
                relativeX,
                relativeY,
                width,
                height
              } = await NativeModules.MyIntentModule.dataToJSByPromise(10, 100);
              alert(relativeX + ':' + relativeY + ':' + width + ':' + height);
            } catch (e) {
              console.error(e);
            }
          }}
        >
          <Text>调用原生 dataToJSByPromise 方法</Text>
        </TouchableNativeFeedback>
      </View>
    );
  }
}
var styles = StyleSheet.create({
  container: {
    flex: 1,
    borderWidth: 1,
    // borderColor: 'red',
    justifyContent: 'center'
  },
  hello: {
    fontSize: 20,
    textAlign: 'center',
  }
});

export default HelloWorld;
// module.exports = HelloWorld;
