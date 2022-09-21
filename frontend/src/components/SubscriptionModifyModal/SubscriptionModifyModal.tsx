import { useTheme } from '@emotion/react';
import { AxiosResponse } from 'axios';
import { UseMutateFunction, useMutation, useQueryClient } from 'react-query';
import { useRecoilValue } from 'recoil';

import useToggle from '@/hooks/useToggle';

import { ModalPosType } from '@/@types';
import { SubscriptionType } from '@/@types/subscription';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';
import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import CategoryModifyModal from '@/components/CategoryModifyModal/CategoryModifyModal';

import { CACHE_KEY } from '@/constants/api';
import { CATEGORY_TYPE } from '@/constants/category';
import { CONFIRM_MESSAGE } from '@/constants/message';
import { PALETTE } from '@/constants/style';

import categoryApi from '@/api/category';

import { MdDeleteOutline, MdOutlineModeEdit } from 'react-icons/md';

import {
  colorStyle,
  controlButtonStyle,
  modalPosStyle,
  outerStyle,
  paletteStyle,
} from './SubscriptionModifyModal.styles';

interface SubscriptionModifyModalProps {
  togglePaletteOpen: () => void;
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
  togglePaletteOpen,
  modalPos,
  subscription,
  patchSubscription,
}: SubscriptionModifyModalProps) {
  const theme = useTheme();

  const user = useRecoilValue(userState);

  const queryClient = useQueryClient();

  const { state: isCategoryModifyModalOpen, toggleState: toggleCategoryModifyModalOpen } =
    useToggle();

  const { mutate: deleteSubscription } = useMutation(
    () => categoryApi.delete(user.accessToken, subscription.category.id),
    {
      onSuccess: () => onSuccessDeleteCategory(),
    }
  );

  const onSuccessDeleteCategory = () => {
    queryClient.invalidateQueries(CACHE_KEY.CATEGORIES);
    queryClient.invalidateQueries(CACHE_KEY.MY_CATEGORIES);
    queryClient.invalidateQueries(CACHE_KEY.SUBSCRIPTIONS);
  };

  const handleClickDeleteButton = () => {
    if (confirm(CONFIRM_MESSAGE.DELETE)) {
      deleteSubscription();
    }
  };

  const handleClickPalette = (checked: boolean, colorCode: string) => {
    patchSubscription({ checked, colorCode });
    togglePaletteOpen();
  };

  const canEditSubscription =
    subscription.category.categoryType !== CATEGORY_TYPE.PERSONAL &&
    subscription.category.creator.id === user.id;

  return (
    <>
      <div css={outerStyle} onClick={togglePaletteOpen} />
      <div css={modalPosStyle(theme, modalPos)}>
        {canEditSubscription && (
          <>
            <Button cssProp={controlButtonStyle} onClick={toggleCategoryModifyModalOpen}>
              <MdOutlineModeEdit size={20} />
              <span>수정</span>
            </Button>
            <Button cssProp={controlButtonStyle} onClick={handleClickDeleteButton}>
              <MdDeleteOutline size={20} />
              <span>삭제</span>
            </Button>
          </>
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
      <ModalPortal isOpen={isCategoryModifyModalOpen} closeModal={toggleCategoryModifyModalOpen}>
        <CategoryModifyModal
          category={subscription.category}
          closeModal={() => {
            togglePaletteOpen();
            toggleCategoryModifyModalOpen();
          }}
        />
      </ModalPortal>
    </>
  );
}

export default SubscriptionModifyModal;
