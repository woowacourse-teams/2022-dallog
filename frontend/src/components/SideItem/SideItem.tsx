import { useMutation, useQueryClient } from 'react-query';
import { useRecoilValue } from 'recoil';

import useModalPosition from '@/hooks/useModalPosition';

import { SubscriptionType } from '@/@types/subscription';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';
import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import Spinner from '@/components/@common/Spinner/Spinner';
import SubscriptionModifyModal from '@/components/SubscriptionModifyModal/SubscriptionModifyModal';

import { TRANSPARENT } from '@/constants/style';

import subscriptionApi from '@/api/subscription';

import { MdCheckBox, MdCheckBoxOutlineBlank, MdMoreVert } from 'react-icons/md';

import {
  checkBoxNameStyle,
  iconStyle,
  itemStyle,
  modalLayoutStyle,
  nameStyle,
} from './SideItem.styles';

interface SideItemProps {
  subscription: SubscriptionType;
}

function SideItem({ subscription }: SideItemProps) {
  const user = useRecoilValue(userState);

  const queryClient = useQueryClient();

  const { isLoading, mutate: patchSubscription } = useMutation(
    (body: Pick<SubscriptionType, 'colorCode'> | Pick<SubscriptionType, 'checked'>) =>
      subscriptionApi.patch(user.accessToken, subscription.id, body),
    {
      onSuccess: () => queryClient.invalidateQueries(),
    }
  );

  const {
    state: isPaletteOpen,
    toggleState: togglePaletteOpen,
    handleClickOpenButton,
    modalPos,
  } = useModalPosition();

  const handleClickCategoryItem = (checked: boolean, colorCode: string) => {
    patchSubscription({
      checked: !checked,
      colorCode,
    });
  };

  return (
    <div css={itemStyle}>
      <div css={checkBoxNameStyle}>
        {isLoading ? (
          <Spinner />
        ) : subscription.checked ? (
          <MdCheckBox
            size={20}
            color={subscription.colorCode}
            onClick={() => {
              handleClickCategoryItem(subscription.checked, subscription.colorCode);
            }}
          />
        ) : (
          <MdCheckBoxOutlineBlank
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
          <MdMoreVert size={20} onClick={handleClickOpenButton} />
        </Button>
        {isPaletteOpen && (
          <ModalPortal
            isOpen={isPaletteOpen}
            closeModal={togglePaletteOpen}
            dimmerBackground={TRANSPARENT}
          >
            <SubscriptionModifyModal
              togglePaletteOpen={togglePaletteOpen}
              modalPos={modalPos}
              subscription={subscription}
              patchSubscription={patchSubscription}
            />
          </ModalPortal>
        )}
      </div>
    </div>
  );
}

export default SideItem;
