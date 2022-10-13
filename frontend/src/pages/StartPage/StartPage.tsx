import { useTheme } from '@emotion/react';

import { useGetLoginUrl } from '@/hooks/@queries/login';

import Button from '@/components/@common/Button/Button';
import PageLayout from '@/components/@common/PageLayout/PageLayout';
import Footer from '@/components/Footer/Footer';

import { getThisDate } from '@/utils/date';

import { FcGoogle } from 'react-icons/fc';

import {
  blackTextStyle,
  calendarStyle,
  dateItemStyle,
  detailTextStyle,
  firstItemStyle,
  googleLoginButton,
  introductionStyle,
  loginText,
  mainContentStyle,
  pageStyle,
  secondItemStyle,
  sectionStyle,
  thirdItemStyle,
  whiteTextStyle,
} from './StartPage.styles';

function StartPage() {
  const theme = useTheme();

  const { error, refetch } = useGetLoginUrl();

  const handleClickGoogleLoginButton = () => {
    refetch();
  };

  if (error) {
    return <>Error</>;
  }

  return (
    <PageLayout>
      <div css={pageStyle}>
        <section css={sectionStyle}>
          <div css={calendarStyle}>
            <div css={dateItemStyle}>{getThisDate()}</div>
            <div css={firstItemStyle}>운동 일정</div>
            <div css={secondItemStyle}>스터디 일정</div>
            <div css={thirdItemStyle}>동아리 일정</div>
          </div>

          <div css={introductionStyle}>
            <div css={mainContentStyle}>
              <section>
                <span css={blackTextStyle}>달력</span>
                <span css={whiteTextStyle}>이</span>
                <br />
                <span css={blackTextStyle}>기록</span>
                <span css={whiteTextStyle}>을</span>
                <br />
                <span css={whiteTextStyle}>공유할때</span>
                <br />
                <span css={blackTextStyle}>달록</span>
                <br />
              </section>
              <span css={detailTextStyle}>
                공유 캘린더 플랫폼 달록은 달력과 카테고리를 <br />
                이용하여 누구나 자신의 일정을 공유할 수 있습니다.
              </span>
              <Button cssProp={googleLoginButton(theme)} onClick={handleClickGoogleLoginButton}>
                <FcGoogle size={20} />
                <p css={loginText}>Google 계정으로 로그인하기</p>
              </Button>
            </div>
          </div>
        </section>
        <Footer />
      </div>
    </PageLayout>
  );
}

export default StartPage;
