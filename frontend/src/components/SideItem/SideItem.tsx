import { useTheme } from '@emotion/react';
import { useMutation, useQueryClient } from 'react-query';
import { useRecoilValue } from 'recoil';

import useModalPosition from '@/hooks/useModalPosition';
import useToggle from '@/hooks/useToggle';

import { SubscriptionType } from '@/@types/subscription';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';
import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import Spinner from '@/components/@common/Spinner/Spinner';
import CategoryModifyModal from '@/components/CategoryModifyModal/CategoryModifyModal';

import { CACHE_KEY } from '@/constants/api';
import { CATEGORY_TYPE } from '@/constants/category';
import { CONFIRM_MESSAGE } from '@/constants/message';
import { PALETTE, TRANSPARENT } from '@/constants/style';

import categoryApi from '@/api/category';
import subscriptionApi from '@/api/subscription';

import { BiDotsVerticalRounded } from 'react-icons/bi';
import { FiEdit3 } from 'react-icons/fi';
import { RiCheckboxBlankLine, RiCheckboxFill, RiDeleteBin5Line } from 'react-icons/ri';

import {
  checkBoxNameStyle,
  colorStyle,
  controlButtonStyle,
  iconStyle,
  itemStyle,
  modalLayoutStyle,
  modalPosStyle,
  nameStyle,
  outerStyle,
  paletteStyle,
} from './SideItem.styles';

interface SideItemProps {
  subscription: SubscriptionType;
}

function SideItem({ subscription }: SideItemProps) {
  const theme = useTheme();

  const user = useRecoilValue(userState);

  const {
    state: isPaletteOpen,
    toggleState: togglePaletteOpen,
    handleClickOpenButton,
    modalPos,
  } = useModalPosition();

  const queryClient = useQueryClient();

  const { isLoading, mutate: patchSubscription } = useMutation(
    (body: Pick<SubscriptionType, 'colorCode'> | Pick<SubscriptionType, 'checked'>) =>
      subscriptionApi.patch(user.accessToken, subscription.id, body),
    {
      onSuccess: () => queryClient.invalidateQueries(),
    }
  );

  const { mutate: deleteSubscription } = useMutation(
    () => categoryApi.delete(user.accessToken, subscription.category.id),
    {
      onSuccess: () => onSuccessDeleteCategory(),
    }
  );

  const { state: isCategoryModifyModalOpen, toggleState: toggleCategoryModifyModalOpen } =
    useToggle();

  const handleClickDeleteButton = () => {
    if (confirm(CONFIRM_MESSAGE.DELETE)) {
      deleteSubscription();
    }
  };

  const onSuccessDeleteCategory = () => {
    queryClient.invalidateQueries(CACHE_KEY.CATEGORIES);
    queryClient.invalidateQueries(CACHE_KEY.MY_CATEGORIES);
    queryClient.invalidateQueries(CACHE_KEY.SUBSCRIPTIONS);
  };
  const handleClickCategoryItem = (checked: boolean, colorCode: string) => {
    patchSubscription({
      checked: !checked,
      colorCode,
    });
  };

  const handleClickPalette = (checked: boolean, colorCode: string) => {
    patchSubscription({ checked, colorCode });
    togglePaletteOpen();
  };

  const canEditSubscription =
    subscription.category.categoryType !== CATEGORY_TYPE.PERSONAL &&
    subscription.category.creator.id === user.id;

  return (
    <div css={itemStyle}>
      <div css={checkBoxNameStyle}>
        {isLoading ? (
          <Spinner />
        ) : subscription.checked ? (
          <RiCheckboxFill
            size={20}
            color={subscription.colorCode}
            onClick={() => {
              handleClickCategoryItem(subscription.checked, subscription.colorCode);
            }}
          />
        ) : (
          <RiCheckboxBlankLine
            size={20}
            color={subscription.colorCode}
            onClick={() => {
              handleClickCategoryItem(subscription.checked, subscription.colorCode);
            }}
          />
        )}
        <span
          css={nameStyle}
          onClick={() => {
            handleClickCategoryItem(subscription.checked, subscription.colorCode);
          }}
        >
          {subscription.category.name}
        </span>
      </div>
      <div css={modalLayoutStyle}>
        <Button cssProp={iconStyle}>
          <BiDotsVerticalRounded size={20} onClick={handleClickOpenButton} />
        </Button>
        {isPaletteOpen && (
          <ModalPortal
            isOpen={isPaletteOpen}
            closeModal={togglePaletteOpen}
            dimmerBackground={TRANSPARENT}
          >
            <>
              <div css={outerStyle} onClick={togglePaletteOpen} />
              <div css={modalPosStyle(theme, modalPos)}>
                {canEditSubscription && (
                  <>
                    <Button cssProp={controlButtonStyle} onClick={toggleCategoryModifyModalOpen}>
                      <FiEdit3 size={20} />
                      <span>수정</span>
                    </Button>
                    <Button cssProp={controlButtonStyle} onClick={handleClickDeleteButton}>
                      <RiDeleteBin5Line size={20} />
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
            </>
          </ModalPortal>
        )}
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
    </div>
  );
}

export default SideItem;
