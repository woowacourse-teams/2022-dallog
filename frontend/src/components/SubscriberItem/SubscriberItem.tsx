import { usePatchCategoryRole } from '@/hooks/@queries/category';

import { ProfileType } from '@/@types/profile';

import Button from '@/components/@common/Button/Button';

import { ROLE } from '@/constants/category';
import { CONFIRM_MESSAGE } from '@/constants/message';

import { MdOutlineAdminPanelSettings } from 'react-icons/md';

import {
  adminButtonStyle,
  displayNameStyle,
  profileImageStyle,
  subscriberItemStyle,
} from './SubscriberItem.styles';

interface SubscriberItemProps {
  categoryId: number;
  subscriber: ProfileType;
}

function SubscriberItem({ categoryId, subscriber }: SubscriberItemProps) {
  const { mutate: patchRole } = usePatchCategoryRole({
    categoryId,
    memberId: subscriber.id,
  });

  const handleClickAddRoleButton = () => {
    window.confirm(CONFIRM_MESSAGE.ADD_ADMIN) &&
      patchRole({
        categoryRoleType: ROLE.ADMIN,
      });
  };

  return (
    <div key={subscriber.id} css={subscriberItemStyle}>
      <img src={subscriber.profileImageUrl} alt="프로필 이미지" css={profileImageStyle} />
      <span css={displayNameStyle}>{subscriber.displayName}</span>
      <Button cssProp={adminButtonStyle} onClick={handleClickAddRoleButton}>
        <MdOutlineAdminPanelSettings />
      </Button>
    </div>
  );
}

export default SubscriberItem;
