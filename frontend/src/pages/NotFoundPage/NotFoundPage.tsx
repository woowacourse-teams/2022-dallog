import { useTheme } from '@emotion/react';
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';

import { sideBarSelector } from '@/recoil/selectors';

import Button from '@/components/@common/Button/Button';
import PageLayout from '@/components/@common/PageLayout/PageLayout';

import { PATH } from '@/constants';
import { PAGE_LAYOUT } from '@/constants/style';

import { buttonStyle, layoutStyle, textStyle } from './NotFoundPage.styles';

function NotFoundPage() {
  const theme = useTheme();

  const toggleSideBarOpen = useSetRecoilState(sideBarSelector);

  const navigation = useNavigate();

  const handleClickReturnButton = () => {
    navigation(PATH.MAIN);
  };

  useEffect(() => {
    toggleSideBarOpen(false);
  }, []);

  return (
    <PageLayout type={PAGE_LAYOUT.SIDEBAR}>
      <div css={layoutStyle}>
        <span css={textStyle(theme, '40rem')}>(⊙_⊙?)</span>
        <span css={textStyle(theme, '10rem')}>
          죄송합니다. <br /> 요청하신 페이지를 찾을 수가 없어요.
        </span>

        <Button cssProp={buttonStyle(theme)} onClick={handleClickReturnButton}>
          달록 홈페이지로 돌아가기
        </Button>
      </div>
    </PageLayout>
  );
}

export default NotFoundPage;
