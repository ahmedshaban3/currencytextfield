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

type AdaptivePagerViewProps = {
  style?: ViewStyle;
  text?: string;
  fontColor: string;
  placeholderColor: string;
  currency: string;
  fontSize: number;
  onChangeText?: (event: { nativeEvent: { text: string } }) => void;
};

const ComponentName = 'AdaptivePagerView';

export const AdaptivePagerView =
  UIManager.getViewManagerConfig(ComponentName) != null
    ? requireNativeComponent<AdaptivePagerViewProps>(ComponentName)
    : () => {
        throw new Error(LINKING_ERROR);
      };
