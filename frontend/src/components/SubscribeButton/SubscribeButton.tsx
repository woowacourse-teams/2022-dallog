import { useTheme } from '@emotion/react';

import Button from '@/components/@common/Button/Button';

import { subscribeButton } from './SubscribeButton.styles';

interface SubscribeButtonProps {
  isSubscribing: boolean;
  handleClickSubscribeButton: () => void;
}

function SubscribeButton({ isSubscribing, handleClickSubscribeButton }: SubscribeButtonProps) {
  const theme = useTheme();

  return (
    <Button cssProp={subscribeButton(theme, isSubscribing)} onClick={handleClickSubscribeButton}>
      {isSubscribing ? '구독중' : '구독'}
    </Button>
  );
}

export default SubscribeButton;
