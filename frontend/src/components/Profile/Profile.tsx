import { useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useRecoilValue } from 'recoil';

import { usePatchProfile } from '@/hooks/@queries/profile';
import useToggle from '@/hooks/useToggle';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';
import Fieldset from '@/components/@common/Fieldset/Fieldset';
import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import WithdrawalModal from '@/components/WithdrawalModal/WithdrawalModal';

import { PATH } from '@/constants';
import { CONFIRM_MESSAGE } from '@/constants/message';

import { createPostBody } from '@/utils';
import { removeAccessToken, removeRefreshToken } from '@/utils/storage';

import { MdOutlineCheck, MdOutlineModeEdit } from 'react-icons/md';

import {
  contentStyle,
  emailStyle,
  imageStyle,
  inputStyle,
  layoutStyle,
  logoutButtonStyle,
  menu,
  menuTitle,
  nameButtonStyle,
  nameStyle,
  withdrawalButtonStyle,
} from './Profile.styles';

function Profile() {
  const navigate = useNavigate();

  const user = useRecoilValue(userState);

  const [isEditingName, setEditingName] = useState(false);

  const inputRef = {
    displayName: useRef<HTMLInputElement>(null),
  };

  const { state: isWithdrawalModalOpen, toggleState: toggleWithdrawalModalOpen } = useToggle();

  const { mutate } = usePatchProfile({ accessToken: user.accessToken });

  const handleClickModifyButton = () => {
    setEditingName(true);
  };

  const handleClickCompleteButton = (defaultName: string | undefined) => {
    if (defaultName === undefined) {
      return;
    }

    const body = createPostBody(inputRef);

    if (body.displayName === '') {
      body.displayName = defaultName;
    }

    mutate(body);
    setEditingName(false);
  };

  const handleClickLogoutButton = () => {
    if (window.confirm(CONFIRM_MESSAGE.LOGOUT)) {
      removeAccessToken();
      removeRefreshToken();
      navigate(PATH.MAIN);
      location.reload();
    }
  };

  return (
    <div css={layoutStyle}>
      <img src={user.profileImageUrl} css={imageStyle} alt="프로필 이미지" />
      <div css={contentStyle}>
        {isEditingName ? (
          <form css={nameButtonStyle}>
            <Fieldset
              defaultValue={user.displayName}
              placeholder={user.displayName}
              refProp={inputRef.displayName}
              cssProp={inputStyle}
              autoFocus={true}
            />
            <Button
              type="submit"
              cssProp={menu}
              onClick={() => handleClickCompleteButton(user.displayName)}
            >
              <MdOutlineCheck size={14} />
              <span css={menuTitle}>완료</span>
            </Button>
          </form>
        ) : (
          <div>
            <span css={nameStyle}>{user.displayName}</span>
            <Button cssProp={menu} onClick={handleClickModifyButton}>
              <MdOutlineModeEdit size={14} />
              <span css={menuTitle}>수정</span>
            </Button>
          </div>
        )}
        <span css={emailStyle}>{user.email}</span>
      </div>
      <Button cssProp={logoutButtonStyle} onClick={handleClickLogoutButton}>
        로그아웃
      </Button>

      <Button cssProp={withdrawalButtonStyle} onClick={toggleWithdrawalModalOpen}>
        회원 탈퇴
      </Button>

      <ModalPortal isOpen={isWithdrawalModalOpen} closeModal={toggleWithdrawalModalOpen}>
        <WithdrawalModal closeModal={toggleWithdrawalModalOpen} />
      </ModalPortal>
    </div>
  );
}

export default Profile;
