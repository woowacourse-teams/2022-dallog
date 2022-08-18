import { useTheme } from '@emotion/react';
import { AxiosError, AxiosResponse } from 'axios';
import { useQuery } from 'react-query';
import { useRecoilValue } from 'recoil';

import useControlledInput from '@/hooks/useControlledInput';

import { SchedulingResponseType } from '@/@types/scheduler';
import { SubscriptionType } from '@/@types/subscription';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';
import Fieldset from '@/components/@common/Fieldset/Fieldset';
import PageLayout from '@/components/@common/PageLayout/PageLayout';

import { CACHE_KEY } from '@/constants/api';

import { getDate } from '@/utils/date';

import schedulerApi from '@/api/scheduler';
import subscriptionApi from '@/api/subscription';

import { BsArrowRight } from 'react-icons/bs';
import { GoSearch } from 'react-icons/go';

import {
  dateTimeFieldsetStyle,
  dateTimeStyle,
  formStyle,
  labelStyle,
  pageStyle,
  resultDateTimeStyle,
  resultStyle,
  resultTimeStyle,
  searchButtonStyle,
  subscriptionSelectStyle,
  subscriptionStyle,
} from './SchedulingPage.styles';

function SchedulingPage() {
  const { accessToken } = useRecoilValue(userState);

  const theme = useTheme();

  const { isLoading: isGetSubscriptionsLoading, data: subscriptionsGetResponse } = useQuery<
    AxiosResponse<SubscriptionType[]>,
    AxiosError
  >(CACHE_KEY.SUBSCRIPTIONS, () => subscriptionApi.get(accessToken), {
    onSuccess: (data) => onSuccessGetSubscriptions(data),
  });

  const category = useControlledInput();
  const startDateTime = useControlledInput(getDate(null));
  const endDateTime = useControlledInput(getDate(null));

  const onSuccessGetSubscriptions = (data: AxiosResponse<SubscriptionType[]>) => {
    category.setInputValue(`${data.data[0].category.id}`);
    refetch();
  };

  const { data: schedulingGetResponse, refetch } = useQuery<
    AxiosResponse<SchedulingResponseType[]>,
    AxiosError
  >(
    CACHE_KEY.SCHEDULER,
    () =>
      schedulerApi.get(
        accessToken,
        Number(category.inputValue),
        startDateTime.inputValue,
        endDateTime.inputValue
      ),
    {
      enabled: false,
    }
  );

  if (isGetSubscriptionsLoading || subscriptionsGetResponse === undefined) {
    return <></>;
  }

  const handleClickSearchButton = () => {
    refetch();
  };

  const formatDateTime = (dateTime: string) => {
    return dateTime.replace('T', ' ').slice(0, -3);
  };

  return (
    <PageLayout>
      <div css={pageStyle}>
        <form css={formStyle}>
          <div css={subscriptionStyle}>
            <span css={labelStyle}>조회할 카테고리</span>
            <select
              css={subscriptionSelectStyle}
              value={category.inputValue}
              onChange={category.onChangeValue}
            >
              {subscriptionsGetResponse.data.map((subscription) => (
                <option key={subscription.category.id} value={subscription.category.id}>
                  {subscription.category.name}
                </option>
              ))}
            </select>
          </div>
          <div css={dateTimeStyle}>
            <Fieldset
              type="date"
              cssProp={{ div: dateTimeFieldsetStyle, label: labelStyle(theme) }}
              labelText="조회 시작 날짜"
              value={startDateTime.inputValue}
              onChange={startDateTime.onChangeValue}
            />
            <BsArrowRight size={40} />
            <Fieldset
              type="date"
              cssProp={{ div: dateTimeFieldsetStyle, label: labelStyle(theme) }}
              labelText="끝 날짜"
              value={endDateTime.inputValue}
              onChange={endDateTime.onChangeValue}
            />
          </div>
        </form>
        <Button cssProp={searchButtonStyle(theme)} onClick={handleClickSearchButton}>
          <GoSearch size={20} />
          <span>카테고리 구독자들 모두 가능한 시간 조회하기</span>
        </Button>
        <div css={resultStyle}>
          {schedulingGetResponse &&
            schedulingGetResponse.data.map((schedule) => (
              <div key={schedule.startDateTime} css={resultTimeStyle}>
                <div css={resultDateTimeStyle}>{formatDateTime(schedule.startDateTime)}</div>
                <BsArrowRight size={40} />
                <div css={resultDateTimeStyle}>{formatDateTime(schedule.endDateTime)}</div>
              </div>
            ))}
        </div>
      </div>
    </PageLayout>
  );
}

export default SchedulingPage;
