import { useTheme } from '@emotion/react';
import { AxiosResponse } from 'axios';
import { UseMutateFunction } from 'react-query';

import { useGetEditableCategories } from '@/hooks/@queries/category';
import { useDeleteSubscriptions } from '@/hooks/@queries/subscription';
import useToggle from '@/hooks/useToggle';

import { ModalPosType } from '@/@types';
import { SubscriptionType } from '@/@types/subscription';

import Button from '@/components/@common/Button/Button';
import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import AdminCategoryManageModal from '@/components/AdminCategoryManageModal/AdminCategoryManageModal';
import GoogleCategoryManageModal from '@/components/GoogleCategoryManageModal/GoogleCategoryManageModal';

import { CATEGORY_TYPE } from '@/constants/category';
import { CONFIRM_MESSAGE } from '@/constants/message';
import { PALETTE } from '@/constants/style';

import { getRootFontSize } from '@/utils';

import { MdOutlineDelete, MdSettings } from 'react-icons/md';

import {
  colorStyle,
  controlButtonStyle,
  modalPosStyle,
  outerStyle,
  paletteStyle,
} from './SubscriptionModifyModal.styles';

interface SubscriptionModifyModalProps {
  toggleModalOpen: () => void;
  modalPos: ModalPosType;
  subscription: SubscriptionType;
  patchSubscription: UseMutateFunction<
    AxiosResponse<null>,
    unknown,
    Pick<SubscriptionType, 'colorCode'> | Pick<SubscriptionType, 'checked'>,
    unknown
  >;
}

function SubscriptionModifyModal({
  toggleModalOpen,
  modalPos,
  subscription,
  patchSubscription,
}: SubscriptionModifyModalProps) {
  const theme = useTheme();

  const { state: isCategoryManageModalOpen, toggleState: toggleCategoryManageModalOpen } =
    useToggle();

  const { isLoading, data } = useGetEditableCategories({});

  const { mutate } = useDeleteSubscriptions({
    subscriptionId: subscription.id,
    onSuccess: toggleModalOpen,
  });

  if (isLoading || !data) {
    return <></>;
  }

  const handleClickManageButton = () => {
    toggleCategoryManageModalOpen();
  };

  const handleClickPalette = (checked: boolean, colorCode: string) => {
    patchSubscription({ checked, colorCode });
    toggleModalOpen();
  };

  const handleClickDeleteSubscription = () => {
    if (window.confirm(CONFIRM_MESSAGE.UNSUBSCRIBE)) mutate();
  };

  const closeModals = () => {
    toggleCategoryManageModalOpen();
    toggleModalOpen();
  };

  const canEditCategories = data.data
    .filter((category) => category.categoryType !== CATEGORY_TYPE.PERSONAL)
    .map((category) => category.id);

  const canEditSubscription = canEditCategories.includes(subscription.category.id);

  const canDeleteSubscription =
    !canEditCategories.includes(subscription.category.id) &&
    subscription.category.categoryType !== CATEGORY_TYPE.PERSONAL;

  const rootFontSize = getRootFontSize();

  return (
    <>
      <div css={outerStyle} onClick={toggleModalOpen} />
      <div css={modalPosStyle(theme, modalPos)}>
        {canEditSubscription && (
          <Button cssProp={controlButtonStyle} onClick={handleClickManageButton}>
            <MdSettings size={rootFontSize * 5} />
            <span>관리</span>
          </Button>
        )}
        {canDeleteSubscription && (
          <Button cssProp={controlButtonStyle} onClick={handleClickDeleteSubscription}>
            <MdOutlineDelete size={rootFontSize * 5} />
            <span>구독 해제</span>
          </Button>
        )}
        <div css={paletteStyle}>
          {PALETTE.map((color) => {
            return (
              <Button
                key={color}
                cssProp={colorStyle(color)}
                onClick={() => {
                  handleClickPalette(subscription.checked, color);
                }}
              ></Button>
            );
          })}
        </div>
      </div>

      <ModalPortal isOpen={isCategoryManageModalOpen} closeModal={closeModals}>
        {subscription.category.categoryType === CATEGORY_TYPE.GOOGLE ? (
          <GoogleCategoryManageModal subscription={subscription} closeModal={closeModals} />
        ) : (
          <AdminCategoryManageModal subscription={subscription} closeModal={closeModals} />
        )}
      </ModalPortal>
    </>
  );
}

export default SubscriptionModifyModal;
