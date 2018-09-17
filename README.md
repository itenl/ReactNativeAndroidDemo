# ReactNativeAndroidDemo

ReactNative 基于 Android 中的集成、两端事件调用、Fragment 集成、Callback/Promise 使用

## Usage

step 1：通过 clone 本项目后 `npm install` 安装相关依赖；

step 2：修改 `~/ReactNativeAndroidDemo/local.properties` 下的 `sdk.dir=/Users/[your computer name]/Library/Android/sdk` ；

step 3：`npm start` 即可

```javascript
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
    ToastAndroid.show('JS界面:错误信息为:' + result, ToastAndroid.SHORT);
  }
);

项目中使用的 [com.example.cmacking...] 为 Android 项目命名空间
```
