import { useTheme } from '@emotion/react';
import { useRecoilValue } from 'recoil';

import { useGetSubscriptions } from '@/hooks/@queries/subscription';

import { sideBarState, userState } from '@/recoil/atoms';

import Spinner from '@/components/@common/Spinner/Spinner';
import SideGoogleList from '@/components/SideGoogleList/SideGoogleList';
import SideMyList from '@/components/SideMyList/SideMyList';
import SideSubscribedList from '@/components/SideSubscribedList/SideSubscribedList';

import { CATEGORY_TYPE } from '@/constants/category';

import { sideBar } from './SideBar.styles';

function SideBar() {
  const user = useRecoilValue(userState);
  const isSideBarOpen = useRecoilValue(sideBarState);

  const theme = useTheme();

  const { isLoading, data } = useGetSubscriptions({ enabled: isSideBarOpen && !!user.accessToken });

  if (!user.accessToken || isLoading || data === undefined) {
    return (
      <div css={sideBar(theme, isSideBarOpen)}>
        <Spinner />
      </div>
    );
  }

  const subscribedList = data.data.filter((el) => el.category.creator.id !== user.id);

  const myList = data.data.filter(
    (el) => el.category.categoryType !== CATEGORY_TYPE.GOOGLE && el.category.creator.id === user.id
  );

  const googleList = data.data.filter((el) => el.category.categoryType === CATEGORY_TYPE.GOOGLE);

  return (
    <div css={sideBar(theme, isSideBarOpen)}>
      <SideMyList categories={myList} />
      <SideSubscribedList categories={subscribedList} />
      <SideGoogleList categories={googleList} />
    </div>
  );
}

export default SideBar;
