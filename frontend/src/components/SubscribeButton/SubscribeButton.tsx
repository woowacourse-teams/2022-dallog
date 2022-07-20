import { useTheme } from '@emotion/react';
import { useState } from 'react';

import Button from '@/components/@common/Button/Button';

import { subscribeButton } from './SubscribeButton.styles';

function SubscribeButton() {
  const [isSubscribe, setSubscribe] = useState(false);

  const theme = useTheme();

  return (
    <Button cssProp={subscribeButton(theme, isSubscribe)}>{isSubscribe ? '구독중' : '구독'}</Button>
  );
}

export default SubscribeButton;
