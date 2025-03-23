import {
  requireNativeComponent,
  UIManager,
  Platform,
  type ViewStyle,
} from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-my-react-native-package1' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

type MyReactNativePackage1Props = {
  style?: ViewStyle;
  text?: string;
  fontColor: string;
  placeholderColor: string;
  onChangeText?: (event: { nativeEvent: { text: string } }) => void;
};

const ComponentName = 'MyReactNativePackage1View';

export const MyReactNativePackage1View =
  UIManager.getViewManagerConfig(ComponentName) != null
    ? requireNativeComponent<MyReactNativePackage1Props>(ComponentName)
    : () => {
        throw new Error(LINKING_ERROR);
      };
