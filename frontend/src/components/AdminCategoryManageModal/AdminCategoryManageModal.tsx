import { useRecoilValue } from 'recoil';

import {
  useDeleteCategory,
  useGetSubscribers,
  usePatchCategoryName,
  usePatchCategoryRole,
} from '@/hooks/@queries/category';
import { useDeleteSubscriptions } from '@/hooks/@queries/subscription';
import useValidateCategory from '@/hooks/useValidateCategory';

import { SubscriptionType } from '@/@types/subscription';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';
import Fieldset from '@/components/@common/Fieldset/Fieldset';
import Spinner from '@/components/@common/Spinner/Spinner';
import AdminItem from '@/components/AdminItem/AdminItem';
import SubscriberItem from '@/components/SubscriberItem/SubscriberItem';

import { ROLE } from '@/constants/category';
import { CONFIRM_MESSAGE } from '@/constants/message';

import { MdClose } from 'react-icons/md';

import {
  closeModalButtonStyle,
  deleteButtonStyle,
  forgiveButtonStyle,
  headerStyle,
  layoutStyle,
  listBundleStyle,
  listSectionStyle,
  renameButtonStyle,
  renameFieldSetStyle,
  renameFormStyle,
  sectionStyle,
  spaceBetweenStyle,
  subscriberListStyle,
  titleStyle,
} from './AdminCategoryManageModal.styles';

interface AdminCategoryManageModalProps {
  subscription: SubscriptionType;
  closeModal: () => void;
}

function AdminCategoryManageModal({ subscription, closeModal }: AdminCategoryManageModalProps) {
  const { id } = useRecoilValue(userState);

  const { categoryValue, getCategoryErrorMessage, isValidCategory } = useValidateCategory(
    subscription.category.name
  );

  const { isLoading, data } = useGetSubscribers({ categoryId: subscription.category.id });

  const { mutate: patchCategoryName } = usePatchCategoryName({
    categoryId: subscription.category.id,
  });

  const { mutate: deleteCategory } = useDeleteCategory({
    categoryId: subscription.category.id,
    onSuccess: closeModal,
  });

  const { mutate: patchRole } = usePatchCategoryRole({
    categoryId: subscription.category.id,
    memberId: Number(id),
    onSuccess: () => {
      if (!window.confirm(CONFIRM_MESSAGE.UNSUBSCRIBE)) return;

      deleteSubscription();
    },
  });

  const { mutate: deleteSubscription } = useDeleteSubscriptions({
    subscriptionId: subscription.id,
    onSuccess: closeModal,
  });

  if (isLoading || data === undefined) {
    return <Spinner size={10} />;
  }

  const handleSubmitCategoryModifyForm = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    patchCategoryName({ name: categoryValue.inputValue });
  };

  const handleClickDeleteCategoryButton = () => {
    window.confirm(CONFIRM_MESSAGE.DELETE) && deleteCategory();
  };

  const handleClickForgiveAdminButton = () => {
    if (!window.confirm(CONFIRM_MESSAGE.FORGIVE_ADMIN)) return;

    patchRole({
      categoryRoleType: ROLE.NONE,
    });
  };

  const admins = data.data.filter((member) => member.categoryRoleType === ROLE.ADMIN);
  const subscribers = data.data.filter((member) => member.categoryRoleType === ROLE.NONE);

  return (
    <div css={layoutStyle}>
      <h1 css={headerStyle}>{subscription.category.name} (관리)</h1>
      <Button cssProp={closeModalButtonStyle} onClick={closeModal}>
        <MdClose />
      </Button>
      <section css={sectionStyle}>
        <h2 css={titleStyle}>카테고리 이름 수정</h2>
        <form css={renameFormStyle} onSubmit={handleSubmitCategoryModifyForm}>
          <Fieldset
            placeholder={subscription.category.name}
            value={categoryValue.inputValue}
            autoFocus
            onChange={categoryValue.onChangeValue}
            isValid={isValidCategory}
            errorMessage={getCategoryErrorMessage()}
            cssProp={renameFieldSetStyle}
          />
          <Button type="submit" disabled={!isValidCategory} cssProp={renameButtonStyle}>
            수정
          </Button>
        </form>
      </section>

      <div css={listBundleStyle}>
        <section css={listSectionStyle}>
          <h2 css={titleStyle}>편집자 목록</h2>
          <span>편집 권한을 해제할 수 있습니다.</span>
          <div css={subscriberListStyle}>
            {admins.map((admin) => {
              return (
                <AdminItem
                  key={admin.member.id}
                  categoryId={subscription.category.id}
                  admin={admin.member}
                />
              );
            })}
          </div>
        </section>
        <section css={listSectionStyle}>
          <h2 css={titleStyle}>구독자 목록</h2>
          <span>편집 권한을 설정할 수 있습니다.</span>
          {subscribers.length === 0 ? (
            <span>하지만 편집자를 제외한 구독자가 아무도 없네요.</span>
          ) : (
            <div css={subscriberListStyle}>
              {subscribers.map((subscriber) => {
                return (
                  <SubscriberItem
                    key={subscriber.member.id}
                    categoryId={subscription.category.id}
                    subscriber={subscriber.member}
                  />
                );
              })}
            </div>
          )}
        </section>
      </div>

      <section css={sectionStyle}>
        <h2 css={titleStyle}>카테고리 삭제</h2>
        <div css={spaceBetweenStyle}>
          <span>카테고리를 영구적으로 삭제합니다.</span>
          <Button onClick={handleClickDeleteCategoryButton} cssProp={deleteButtonStyle}>
            삭제
          </Button>
        </div>
      </section>

      <section css={sectionStyle}>
        <h2 css={titleStyle}>관리 권한 포기</h2>
        <div css={spaceBetweenStyle}>
          <span>일정 추가/삭제/수정 및 카테고리 수정/삭제 권한을 포기합니다.</span>
          <Button cssProp={forgiveButtonStyle} onClick={handleClickForgiveAdminButton}>
            포기
          </Button>
        </div>
      </section>
    </div>
  );
}

export default AdminCategoryManageModal;
