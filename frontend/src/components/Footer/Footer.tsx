import { useNavigate } from 'react-router-dom';

import Button from '@/components/@common/Button/Button';

import { PATH } from '@/constants';

import { footerStyle, privacyPolicyButtonStyle } from './Footer.styles';

function Footer() {
  const navigate = useNavigate();

  const handleClickPrivacyPolicyButton = () => {
    navigate(PATH.POLICY);
  };

  return (
    <footer css={footerStyle}>
      <p>우아한테크코스 4기 달록</p>
      <p>서울특별시 송파구 올림픽로35다길 42, 14층 (한국루터회관)</p>
      <p>Copyright © 2022 달록 - All rights reserved.</p>
      <Button cssProp={privacyPolicyButtonStyle} onClick={handleClickPrivacyPolicyButton}>
        개인정보처리방침
      </Button>
    </footer>
  );
}

export default Footer;
