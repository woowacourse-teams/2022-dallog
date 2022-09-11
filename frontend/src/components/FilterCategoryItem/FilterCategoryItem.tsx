import { useTheme } from '@emotion/react';
import { useMutation, useQueryClient } from 'react-query';
import { useRecoilValue } from 'recoil';

import useModalPosition from '@/hooks/useModalPosition';

import { SubscriptionType } from '@/@types/subscription';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';
import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import Spinner from '@/components/@common/Spinner/Spinner';

import { PALETTE, TRANSPARENT } from '@/constants/style';

import subscriptionApi from '@/api/subscription';

import { BiPalette } from 'react-icons/bi';
import { RiCheckboxBlankLine, RiCheckboxFill } from 'react-icons/ri';

import {
  checkBoxNameStyle,
  colorStyle,
  iconStyle,
  itemStyle,
  nameStyle,
  outerStyle,
  paletteLayoutStyle,
  paletteStyle,
} from './FilterCategoryItem.styles';

interface FilterItemProps {
  subscription: SubscriptionType;
}

function FilterCategoryItem({ subscription }: FilterItemProps) {
  const theme = useTheme();

  const { accessToken } = useRecoilValue(userState);

  const {
    state: isPaletteOpen,
    toggleState: togglePaletteOpen,
    handleClickOpenButton,
    modalPos,
  } = useModalPosition();

  const queryClient = useQueryClient();
  const { isLoading, mutate } = useMutation(
    (body: Pick<SubscriptionType, 'colorCode'> | Pick<SubscriptionType, 'checked'>) =>
      subscriptionApi.patch(accessToken, subscription.id, body),
    {
      onSuccess: () => queryClient.invalidateQueries(),
    }
  );

  const handleClickCategoryItem = (checked: boolean, colorCode: string) => {
    mutate({
      checked: !checked,
      colorCode,
    });
  };

  const handleClickPalette = (checked: boolean, colorCode: string) => {
    mutate({ checked, colorCode });
    togglePaletteOpen();
  };

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
      <div css={paletteLayoutStyle}>
        <Button cssProp={iconStyle}>
          <BiPalette size={20} onClick={handleClickOpenButton} />
        </Button>
        {isPaletteOpen && (
          <ModalPortal
            isOpen={isPaletteOpen}
            closeModal={togglePaletteOpen}
            dimmerBackground={TRANSPARENT}
          >
            <>
              <div css={outerStyle} onClick={togglePaletteOpen} />
              <div css={paletteStyle(theme, modalPos)}>
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
            </>
          </ModalPortal>
        )}
      </div>
    </div>
  );
}

export default FilterCategoryItem;
