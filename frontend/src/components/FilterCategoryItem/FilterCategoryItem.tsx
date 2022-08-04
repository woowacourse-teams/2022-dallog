import { useMutation, useQueryClient } from 'react-query';
import { useRecoilValue } from 'recoil';

import useToggle from '@/hooks/useToggle';

import { SubscriptionType } from '@/@types/subscription';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';

import { CACHE_KEY, PALETTE } from '@/constants';

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
  const { accessToken } = useRecoilValue(userState);
  const { state: isPaletteOpen, toggleState: togglePaletteOpen } = useToggle();

  const queryClient = useQueryClient();

  const { mutate } = useMutation(
    (body: Pick<SubscriptionType, 'color'> | Pick<SubscriptionType, 'checked'>) =>
      subscriptionApi.patch(accessToken, subscription.id, body),
    {
      onSuccess: () => {
        queryClient.invalidateQueries(CACHE_KEY.SCHEDULES);
        queryClient.invalidateQueries(CACHE_KEY.SUBSCRIPTIONS);
      },
    }
  );

  const handleClickFilledCheckBox = (color: string) => {
    mutate({
      checked: false,
      color,
    });
  };

  const handleClickBlankCheckBox = (color: string) => {
    mutate({
      checked: true,
      color,
    });
  };

  const handleClickPalette = (checked: boolean, color: string) => {
    mutate({ checked, color });
    togglePaletteOpen();
  };

  return (
    <div css={itemStyle}>
      <div css={checkBoxNameStyle}>
        {subscription.checked ? (
          <Button
            onClick={() => {
              handleClickFilledCheckBox(subscription.color);
            }}
          >
            <RiCheckboxFill size={20} color={subscription.color} />
          </Button>
        ) : (
          <Button
            onClick={() => {
              handleClickBlankCheckBox(subscription.color);
            }}
          >
            <RiCheckboxBlankLine size={20} color={subscription.color} />
          </Button>
        )}
        <span css={nameStyle}>{subscription.category.name}</span>
      </div>
      <div css={paletteLayoutStyle}>
        <Button cssProp={iconStyle} onClick={togglePaletteOpen}>
          <BiPalette size={20} />
        </Button>
        {isPaletteOpen && (
          <>
            <div css={outerStyle} onClick={togglePaletteOpen} />
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
          </>
        )}
      </div>
    </div>
  );
}

export default FilterCategoryItem;
