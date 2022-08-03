import { useTheme } from '@emotion/react';
import { AxiosError, AxiosResponse } from 'axios';
import { useRef, useState } from 'react';
import { useMutation, useQuery, useQueryClient } from 'react-query';
import { useNavigate } from 'react-router-dom';
import { useRecoilValue } from 'recoil';

import { ProfileType } from '@/@types/profile';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';
import Fieldset from '@/components/@common/Fieldset/Fieldset';
import PageLayout from '@/components/@common/PageLayout/PageLayout';

import { CACHE_KEY, CONFIRM_MESSAGE, PATH } from '@/constants';

import { clearAccessToken, createPostBody } from '@/utils';

import profileAPI from '@/api/profile';

import { AiOutlineCheck } from 'react-icons/ai';
import { FiEdit3 } from 'react-icons/fi';
import { RiLogoutBoxRLine } from 'react-icons/ri';

import {
  imageInfo,
  imageSize,
  infoTable,
  infoTableHeader,
  inputStyle,
  menu,
  menuTitle,
  myPage,
  nameButtonStyle,
  textInfo,
} from './MyPage.styles';

function MyPage() {
  const theme = useTheme();
  const [isEditingName, setEditingName] = useState(false);

  const inputRef = {
    displayName: useRef<HTMLInputElement>(null),
  };

  const { accessToken } = useRecoilValue(userState);

  const navigate = useNavigate();

  const queryClient = useQueryClient();
  const {
    isLoading,
    error,
    data: profileGetResponse,
  } = useQuery<AxiosResponse<ProfileType>, AxiosError>(CACHE_KEY.PROFILE, () =>
    profileAPI.get(accessToken)
  );

  const { mutate } = useMutation(
    (body: { displayName: string }) => profileAPI.patch(accessToken, body),
    {
      onSuccess: () => {
        queryClient.invalidateQueries(CACHE_KEY.PROFILE);
      },
    }
  );

  if (isLoading || profileGetResponse === undefined) {
    return <div>Loading</div>;
  }

  if (error) {
    return <div>Error</div>;
  }

  const handleClickLogoutButton = () => {
    if (window.confirm(CONFIRM_MESSAGE.LOGOUT)) {
      clearAccessToken();
      navigate(PATH.MAIN);
      location.reload();
    }
  };

  const handleClickModifyButton = () => {
    setEditingName(true);
  };

  const handleClickCompleteButton = (defaultName: string) => {
    const body = createPostBody(inputRef);

    if (body.displayName === '') {
      body.displayName = defaultName;
    }

    mutate(body);
    setEditingName(false);
  };

  const { displayName, email, profileImageUrl } = profileGetResponse.data;

  return (
    <PageLayout>
      <div css={myPage}>
        <div css={infoTable}>
          <div css={infoTableHeader}>
            <span>프로필</span>
            <Button cssProp={menu(theme)} onClick={handleClickLogoutButton}>
              <RiLogoutBoxRLine size={20} />
              <span css={menuTitle}>로그아웃</span>
            </Button>
          </div>
          <div css={textInfo}>
            <span>이름(닉네임)</span>
            <div css={nameButtonStyle}>
              {isEditingName ? (
                <Fieldset
                  cssProp={inputStyle}
                  defaultValue={displayName}
                  placeholder={displayName}
                  refProp={inputRef.displayName}
                />
              ) : (
                <span>{displayName}</span>
              )}
              {isEditingName ? (
                <Button
                  cssProp={menu(theme)}
                  onClick={() => handleClickCompleteButton(displayName)}
                >
                  <AiOutlineCheck size={14} />
                  <span css={menuTitle}>완료</span>
                </Button>
              ) : (
                <Button cssProp={menu(theme)} onClick={handleClickModifyButton}>
                  <FiEdit3 size={14} />
                  <span css={menuTitle}>수정</span>
                </Button>
              )}
            </div>
          </div>
          <div css={textInfo}>
            <span>계정</span>
            <span>{email}</span>
          </div>
          <div css={imageInfo}>
            <span>프로필 사진</span>
            <img src={profileImageUrl} css={imageSize} alt="프로필 이미지" />
          </div>
        </div>
      </div>
    </PageLayout>
  );
}

export default MyPage;
