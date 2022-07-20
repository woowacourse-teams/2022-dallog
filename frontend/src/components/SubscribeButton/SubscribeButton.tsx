import { useTheme } from '@emotion/react';
import { useEffect, useState } from 'react';

import Button from '@/components/@common/Button/Button';

import { subscribeButton } from './SubscribeButton.styles';

interface SubscribeButtonProps {
  isSubscribing: boolean;
}

function SubscribeButton({ isSubscribing }: SubscribeButtonProps) {
  const [isSubscribe, setSubscribe] = useState(isSubscribing);

  const theme = useTheme();

  useEffect(() => {
    setSubscribe(isSubscribing);
  }, [isSubscribing]);

  return (
    <Button cssProp={subscribeButton(theme, isSubscribe)}>{isSubscribe ? '구독중' : '구독'}</Button>
  );
}

export default SubscribeButton;
