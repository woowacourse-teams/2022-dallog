import { useTheme } from '@emotion/react';
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useRecoilState } from 'recoil';

import { sideBarSelector } from '@/recoil/selectors';

import Button from '@/components/@common/Button/Button';

import { PATH } from '@/constants';

import { buttonStyle, layoutStyle, textStyle } from './NotFoundPage.styles';

function NotFoundPage() {
  const theme = useTheme();

  const navigation = useNavigate();

  const [isSideBarOpen, toggleSideBarOpen] = useRecoilState(sideBarSelector);

  const handleClickReturnButton = () => {
    navigation(PATH.MAIN);
  };

  useEffect(() => {
    toggleSideBarOpen(isSideBarOpen);
  }, []);
  return (
    <div css={layoutStyle}>
      <span css={textStyle(theme, '40rem')}>(⊙_⊙;)</span>
      <span css={textStyle(theme, '15rem')}>주소가 잘못되었습니다.</span>

      <Button cssProp={buttonStyle(theme)} onClick={handleClickReturnButton}>
        달록 홈페이지로 돌아가기
      </Button>
    </div>
  );
}

export default NotFoundPage;
