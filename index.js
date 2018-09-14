import { AppRegistry } from 'react-native';
import HelloWorld from './src/HelloWorld';
import TestView from './src/TestView';
import GetAllNativeModules from './src/GetAllNativeModules';
import RnCommonActivity from './src/RnCommonActivity';

AppRegistry.registerComponent('HelloWorld', () => HelloWorld);
AppRegistry.registerComponent('TestView', () => TestView);
AppRegistry.registerComponent('GetAllNativeModules', () => GetAllNativeModules);

AppRegistry.registerComponent('RnCommonActivity', () => RnCommonActivity);
