import { Component } from 'react';

import ErrorPage from '@/pages/ErrorPage/ErrorPage';

interface Props {
  children: JSX.Element | JSX.Element[];
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
    if (this.state.hasError) return <ErrorPage />;

    return this.props.children;
  }
}

export default ErrorBoundary;
