import React, { ReactNode, useRef } from 'react';
import {
  requireNativeComponent,
  UIManager,
  View,
  Platform,
  findNodeHandle,
  StyleProp,
  ViewStyle,
} from 'react-native';

type Props = {
  children: ReactNode;
  style?: StyleProp<ViewStyle>;
  onEndReached: () => void;
};

const ComponentName = 'AdaptiveTableView';

const NativeComponent = requireNativeComponent<Props>(ComponentName);

export default function AdaptiveTableView1({
  children,
  style,
  onEndReached,
}: Props) {
  return (
    <NativeComponent style={style} onEndReached={onEndReached}>
      {React.Children.map(children, (child, index) => (
        <View collapsable={false} key={index}>
          {child}
        </View>
      ))}
    </NativeComponent>
  );
}
