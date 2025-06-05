import React, { useEffect } from 'react';
import Header from './components/Header';
import Breadcrumb from './components/Breadcrumb';
import Footer from './components/Footer';
import SensitiveDataServicePage from './components/SensitiveDataServicePage';

declare global {
  interface Window {
    FreshWidget?: any;
  }
}

function App() {
  useEffect(() => {
    // Initialize Freshdesk widget here, ensuring FreshWidget is available
    if (window.FreshWidget) {
      window.FreshWidget.init("", {
        "queryString": "&widgetType=popup&helpdesk_ticket[group_id]=6000207804&helpdesk_ticket[product_id]=6000005589&formTitle=Report+an+issue+or+ask+for+help",
        "utf8": "✓",
        "widgetType": "popup",
        "buttonType": "text",
        "buttonText": "Need help?",
        "buttonColor": "white",
        "buttonBg": "#d5502a",
        "alignment": "2",
        "offset": "350px",
        "formHeight": "500px",
        "url": "https://support.ala.org.au"
      });
    }
  }, []); // Run once on component mount

  return (
    <div className="App">
      <Header />
      <Breadcrumb />
      {/* Optional banner message can be a separate component or integrated into SensitiveDataServicePage */}
      <div className="ala-admin-message"></div>
      <SensitiveDataServicePage />
      <Footer />
    </div>
  );
}

export default App;