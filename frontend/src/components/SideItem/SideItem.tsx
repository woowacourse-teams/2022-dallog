import { usePatchSubscription } from '@/hooks/@queries/subscription';
import useModalPosition from '@/hooks/useModalPosition';
import useRootFontSize from '@/hooks/useRootFontSize';

import { SubscriptionType } from '@/@types/subscription';

import Button from '@/components/@common/Button/Button';
import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import Spinner from '@/components/@common/Spinner/Spinner';
import SubscriptionModifyModal from '@/components/SubscriptionModifyModal/SubscriptionModifyModal';

import { TRANSPARENT } from '@/constants/style';

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
  const { isLoading, mutate: patchSubscription } = usePatchSubscription({
    subscriptionId: subscription.id,
  });

  const rootFontSize = useRootFontSize();

  const { isModalOpen, toggleModalOpen, handleClickOpen, modalPos } = useModalPosition();

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
            size={rootFontSize * 5}
            color={subscription.colorCode}
            onClick={() => {
              handleClickCategoryItem(subscription.checked, subscription.colorCode);
            }}
          />
        ) : (
          <MdCheckBoxOutlineBlank
            size={rootFontSize * 5}
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
          <MdMoreVert size={rootFontSize * 5} onClick={handleClickOpen} />
        </Button>
        {isModalOpen && (
          <ModalPortal
            isOpen={isModalOpen}
            closeModal={toggleModalOpen}
            dimmerBackground={TRANSPARENT}
          >
            <SubscriptionModifyModal
              toggleModalOpen={toggleModalOpen}
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
