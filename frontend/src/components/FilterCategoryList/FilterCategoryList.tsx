import { useTheme } from '@emotion/react';
import { AxiosError, AxiosResponse } from 'axios';
import { useQuery } from 'react-query';
import { useRecoilValue } from 'recoil';

import useToggle from '@/hooks/useToggle';

import { SubscriptionType } from '@/@types/subscription';

import { sideBarState, userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';
import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import FilterCategoryItem from '@/components/FilterCategoryItem/FilterCategoryItem';
import GoogleImportModal from '@/components/GoogleImportModal/GoogleImportModal';

import { CACHE_KEY } from '@/constants/api';

import subscriptionApi from '@/api/subscription';

import { FcGoogle } from 'react-icons/fc';

import FilterCategoryFallback from './FilterCategoryList.fallback';
import {
  contentStyle,
  googleImportButtonStyle,
  googleImportTextStyle,
  headerStyle,
  listStyle,
} from './FilterCategoryList.styles';

function FilterCategoryList() {
  const { accessToken } = useRecoilValue(userState);
  const isSideBarOpen = useRecoilValue(sideBarState);

  const theme = useTheme();

  const { state: isGoogleImportModalOpen, toggleState: toggleGoogleImportModalOpen } = useToggle();

  const { isLoading, data } = useQuery<AxiosResponse<SubscriptionType[]>, AxiosError>(
    CACHE_KEY.SUBSCRIPTIONS,
    () => subscriptionApi.get(accessToken)
  );

  if (isLoading || data === undefined) {
    return <FilterCategoryFallback />;
  }

  const handleClickGoogleImportButton = () => {
    toggleGoogleImportModalOpen();
  };

  return (
    <div css={listStyle(theme, isSideBarOpen)}>
      <span css={headerStyle}> 구독 카테고리</span>
      <Button cssProp={googleImportButtonStyle(theme)} onClick={handleClickGoogleImportButton}>
        <FcGoogle size={20} />
        <p css={googleImportTextStyle}>구글 캘린더 가져오기</p>
      </Button>
      <div css={contentStyle}>
        {data?.data.map((el) => {
          return <FilterCategoryItem key={el.category.id} subscription={el} />;
        })}
      </div>
      <ModalPortal isOpen={isGoogleImportModalOpen} closeModal={toggleGoogleImportModalOpen}>
        <GoogleImportModal closeModal={toggleGoogleImportModalOpen} />
      </ModalPortal>
    </div>
  );
}

export default FilterCategoryList;
