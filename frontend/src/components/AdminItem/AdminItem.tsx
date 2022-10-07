import { useRecoilValue } from 'recoil';

import { usePatchCategoryRole } from '@/hooks/@queries/category';

import { ProfileType } from '@/@types/profile';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';

import { ROLE } from '@/constants/category';
import { CONFIRM_MESSAGE } from '@/constants/message';

import { MdPersonOff } from 'react-icons/md';

import { adminButtonStyle, adminItemStyle, profileImageStyle } from './AdminItem.styles';

interface AdminItemProps {
  categoryId: number;
  admin: ProfileType;
}

function AdminItem({ categoryId, admin }: AdminItemProps) {
  const { id } = useRecoilValue(userState);

  const { mutate: patchRole } = usePatchCategoryRole({
    categoryId,
    memberId: admin.id,
  });

  const handleClickDeleteRoleButton = () => {
    window.confirm(CONFIRM_MESSAGE.DELETE_ADMIN) &&
      patchRole({
        categoryRoleType: ROLE.NONE,
      });
  };

  return (
    <div key={admin.id} css={adminItemStyle}>
      <img src={admin.profileImageUrl} alt="프로필 이미지" css={profileImageStyle} />
      <span>{admin.displayName}</span>
      {admin.id !== id && (
        <Button cssProp={adminButtonStyle} onClick={handleClickDeleteRoleButton}>
          <MdPersonOff />
        </Button>
      )}
    </div>
  );
}

export default AdminItem;
