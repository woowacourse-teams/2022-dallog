import { validateLength } from '@/validation';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useRecoilValue } from 'recoil';

import { usePatchProfile } from '@/hooks/@queries/profile';
import useControlledInput from '@/hooks/useControlledInput';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';
import Fieldset from '@/components/@common/Fieldset/Fieldset';

import { PATH } from '@/constants';
import { CONFIRM_MESSAGE } from '@/constants/message';
import { VALIDATION_MESSAGE, VALIDATION_SIZE } from '@/constants/validate';

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
} from './Profile.styles';

function Profile() {
  const navigate = useNavigate();

  const user = useRecoilValue(userState);

  const [isEditingName, setEditingName] = useState(false);

  const editDisplayName = useControlledInput(user.displayName);

  const { mutate } = usePatchProfile({ accessToken: user.accessToken });

  const handleClickModifyButton = () => {
    setEditingName(true);
  };

  const handleClickCompleteButton = () => {
    mutate({
      displayName: editDisplayName.inputValue.trim(),
    });

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
              value={editDisplayName.inputValue}
              onChange={editDisplayName.onChangeValue}
              cssProp={inputStyle}
              autoFocus={true}
              isValid={validateLength(
                editDisplayName.inputValue.trim(),
                VALIDATION_SIZE.MIN_LENGTH,
                VALIDATION_SIZE.DISPLAY_NAME_MAX_LENGTH
              )}
              errorMessage={VALIDATION_MESSAGE.STRING_LENGTH(
                VALIDATION_SIZE.MIN_LENGTH,
                VALIDATION_SIZE.DISPLAY_NAME_MAX_LENGTH
              )}
            />
            <Button
              type="submit"
              cssProp={menu}
              onClick={handleClickCompleteButton}
              disabled={
                !validateLength(
                  editDisplayName.inputValue.trim(),
                  VALIDATION_SIZE.MIN_LENGTH,
                  VALIDATION_SIZE.DISPLAY_NAME_MAX_LENGTH
                )
              }
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
    </div>
  );
}

export default Profile;
