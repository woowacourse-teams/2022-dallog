import { validateWithdrawalCondition } from '@/validation';

import { useDeleteProfile } from '@/hooks/@queries/profile';
import useControlledInput from '@/hooks/useControlledInput';

import Button from '@/components/@common/Button/Button';
import Fieldset from '@/components/@common/Fieldset/Fieldset';

import { CONFIRM_MESSAGE } from '@/constants/message';
import { VALIDATION_STRING } from '@/constants/validate';

import { removeAccessToken, removeRefreshToken } from '@/utils/storage';

import {
  headerStyle,
  layoutStyle,
  withdrawalButtonStyle,
  withdrawalConditionTextStyle,
} from './WithdrawalModal.styles';

interface WithdrawalModalProps {
  closeModal: () => void;
}

function WithdrawalModal({ closeModal }: WithdrawalModalProps) {
  const { inputValue, onChangeValue } = useControlledInput();

  const { mutate } = useDeleteProfile({ onSuccess: closeModal });

  const handleClickWithdrawalButton = () => {
    if (window.confirm(CONFIRM_MESSAGE.WITHDRAWAL)) {
      mutate();
    }
  };

  const onSuccessWithdrawalUser = () => {
    closeModal();
    removeAccessToken();
    removeRefreshToken();
    navigate(PATH.MAIN);
    location.reload();
  };

  return (
    <div css={layoutStyle}>
      <h1 css={headerStyle}>달록 탈퇴</h1>
      <p>탈퇴를 진행하면 일정과 카테고리를 비롯한 모든 정보가 영구적으로 삭제됩니다.</p>
      <p>그래도 탈퇴하시겠습니까?</p>
      <p>
        탈퇴를 원하시면{' '}
        <span css={withdrawalConditionTextStyle}>{VALIDATION_STRING.WITHDRAWAL}</span>를
        입력해주세요.
      </p>
      <Fieldset
        placeholder={VALIDATION_STRING.WITHDRAWAL}
        onChange={onChangeValue}
        value={inputValue}
      />
      <Button
        cssProp={withdrawalButtonStyle}
        onClick={handleClickWithdrawalButton}
        disabled={!validateWithdrawalCondition(inputValue)}
      >
        내용을 확인했으며, 탈퇴하겠습니다.
      </Button>
    </div>
  );
}

export default WithdrawalModal;
