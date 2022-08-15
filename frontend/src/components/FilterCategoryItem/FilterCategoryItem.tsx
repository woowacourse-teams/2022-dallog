import { useMutation, useQueryClient } from 'react-query';
import { useRecoilValue } from 'recoil';

import useToggle from '@/hooks/useToggle';

import { SubscriptionType } from '@/@types/subscription';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';

import { CACHE_KEY } from '@/constants';
import { PALETTE } from '@/constants/style';

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
    (body: Pick<SubscriptionType, 'colorCode'> | Pick<SubscriptionType, 'checked'>) =>
      subscriptionApi.patch(accessToken, subscription.id, body),
    {
      onSuccess: () => {
        queryClient.invalidateQueries(CACHE_KEY.SCHEDULES);
        queryClient.invalidateQueries(CACHE_KEY.SUBSCRIPTIONS);
      },
      useErrorBoundary: true,
    }
  );

  const handleClickFilledCheckBox = (colorCode: string) => {
    mutate({
      checked: false,
      colorCode,
    });
  };

  const handleClickBlankCheckBox = (colorCode: string) => {
    mutate({
      checked: true,
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
        {subscription.checked ? (
          <Button
            onClick={() => {
              handleClickFilledCheckBox(subscription.colorCode);
            }}
          >
            <RiCheckboxFill size={20} color={subscription.colorCode} />
          </Button>
        ) : (
          <Button
            onClick={() => {
              handleClickBlankCheckBox(subscription.colorCode);
            }}
          >
            <RiCheckboxBlankLine size={20} color={subscription.colorCode} />
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
