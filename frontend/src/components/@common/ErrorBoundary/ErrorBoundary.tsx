import { Component } from 'react';

import PageLayout from '@/components/@common/PageLayout/PageLayout';

import { ERROR_MESSAGE } from '@/constants/message';

interface Props {
  children: JSX.Element;
}

interface State {
  hasError: boolean;
}

class ErrorBoundary extends Component<Props, State> {
  public state: State = {
    hasError: false,
  };

  public static getDerivedStateFromError(): State {
    return { hasError: true };
  }

  public render() {
    if (this.state.hasError)
      return (
        <PageLayout>
          <span>{ERROR_MESSAGE.DEFAULT}</span>
        </PageLayout>
      );

    return this.props.children;
  }
}

export default ErrorBoundary;
