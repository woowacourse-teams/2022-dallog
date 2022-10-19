import { useTheme } from '@emotion/react';
import { useNavigate } from 'react-router-dom';

import Button from '@/components/@common/Button/Button';

import { PATH } from '@/constants';
import { ERROR_MESSAGE } from '@/constants/message';

import { buttonStyle, layoutStyle, textStyle } from './ErrorPage.styles';

function ErrorPage() {
  const theme = useTheme();

  const navigation = useNavigate();

  const handleClickReturnButton = () => {
    navigation(PATH.MAIN);
    location.reload();
  };

  return (
    <div css={layoutStyle}>
      <span css={textStyle(theme, '40rem')}>((⊙_⊙);)</span>
      <span css={textStyle(theme, '10rem')}>{ERROR_MESSAGE.DEFAULT}</span>

      <Button cssProp={buttonStyle(theme)} onClick={handleClickReturnButton}>
        달록 홈페이지로 돌아가기
      </Button>
    </div>
  );
}

export default ErrorPage;
