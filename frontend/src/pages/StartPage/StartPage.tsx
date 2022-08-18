import { useTheme } from '@emotion/react';
import { useState } from 'react';
import { useQuery } from 'react-query';

import useIntersect from '@/hooks/useIntersect';

import Button from '@/components/@common/Button/Button';
import PageLayout from '@/components/@common/PageLayout/PageLayout';
import Footer from '@/components/Footer/Footer';

import { CACHE_KEY } from '@/constants/api';

import { getThisDate } from '@/utils/date';

import loginApi from '@/api/login';

import { FcGoogle } from 'react-icons/fc';

import howToUse1 from '../../assets/how_to_use_1.png';
import howToUse2 from '../../assets/how_to_use_2.png';
import howToUse3 from '../../assets/how_to_use_3.png';
import {
  blackTextStyle,
  calendarStyle,
  dateItemStyle,
  detailTextStyle,
  firstItemStyle,
  firstSectionStyle,
  googleLoginButton,
  imageStyle,
  introductionStyle,
  loginText,
  mainContentStyle,
  methodHeaderStyle,
  methodItemStyle,
  methodTextStyle,
  pageStyle,
  refStyle,
  secondItemStyle,
  secondSectionStyle,
  thirdItemStyle,
  whiteTextStyle,
} from './StartPage.styles';

function StartPage() {
  const theme = useTheme();

  const [methodAnimation, setMethodAnimation] = useState([false, false, false]);

  const baseMethod = useIntersect(() => {
    setMethodAnimation([false, false, false]);
  });

  const firstMethod = useIntersect(() => {
    setMethodAnimation([true, false, false]);
  });

  const secondMethod = useIntersect(() => {
    setMethodAnimation([true, true, false]);
  });

  const thirdMethod = useIntersect(() => {
    setMethodAnimation([true, true, true]);
  });

  const { error, refetch } = useQuery<string>(CACHE_KEY.ENTER, loginApi.getUrl, {
    enabled: false,
    onSuccess: (data) => onSuccessGetLoginUrl(data),
  });

  const onSuccessGetLoginUrl = (loginUrl: string) => {
    location.href = loginUrl;
  };

  const handleClickGoogleLoginButton = () => {
    refetch();
  };

  if (error) {
    return <>Error</>;
  }

  return (
    <PageLayout>
      <div css={pageStyle}>
        <section css={firstSectionStyle}>
          <div css={calendarStyle}>
            <div css={dateItemStyle}>{getThisDate()}</div>
            <div css={firstItemStyle}>운동 일정</div>
            <div css={secondItemStyle}>스터디 일정</div>
            <div css={thirdItemStyle}>동아리 일정</div>
          </div>
          <div ref={baseMethod} css={refStyle} />

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
        <section css={secondSectionStyle}>
          <span css={methodHeaderStyle}> 달록은 이렇습니다</span>
          <div ref={firstMethod} css={refStyle} />
          <div css={methodItemStyle(theme, methodAnimation[0], 'left')}>
            <img src={howToUse1} alt="달록 사용법 1" css={imageStyle} />
            <span css={methodTextStyle}>
              공유 받고 싶은 카테고리를 구독하여 <br /> 내 달력에서 일정을 볼 수 있습니다.
            </span>
          </div>
          <div ref={secondMethod} css={refStyle} />
          <div css={methodItemStyle(theme, methodAnimation[1], 'right')}>
            <span css={methodTextStyle}>
              구글 캘린더에서 원하는 캘린더를
              <br /> 가져올 수 있습니다.
            </span>
            <img src={howToUse2} alt="달록 사용법 2" css={imageStyle} />
          </div>
          <div ref={thirdMethod} css={refStyle} />
          <div css={methodItemStyle(theme, methodAnimation[2], 'left')}>
            <img src={howToUse3} alt="달록 사용법 3" css={imageStyle} />
            <span css={methodTextStyle}>
              카테고리 내에서 팀원들과 <br />
              겹치지 않는 시간을 <br />
              확인할 수 있습니다.
            </span>
          </div>
        </section>
        <Footer />
      </div>
    </PageLayout>
  );
}

export default StartPage;
