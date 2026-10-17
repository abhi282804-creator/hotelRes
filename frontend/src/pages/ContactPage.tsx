import React, { useState } from 'react';
import { Mail, Phone, MapPin, Send, CheckCircle2 } from 'lucide-react';

export const ContactPage: React.FC = () => {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [subject, setSubject] = useState('');
  const [message, setMessage] = useState('');
  const [submitted, setSubmitted] = useState(false);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setSubmitted(true);
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <div className="text-center max-w-2xl mx-auto mb-12">
        <h1 className="font-serif text-4xl font-bold text-brand-900 mb-3">Contact Concierge Desk</h1>
        <p className="text-sm text-slate-500">
          Have a question about a reservation, property amenities, or custom requests? We're available 24/7.
        </p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-10">

        {/* Info Box */}
        <div className="bg-brand-900 text-white p-8 rounded-3xl space-y-8 shadow-xl">
          <div>
            <h3 className="font-serif text-2xl font-bold mb-2">Get in Touch</h3>
            <p className="text-xs text-slate-400 leading-relaxed">
              Our luxury reservation team is here to ensure your stay is seamless.
            </p>
          </div>

          <div className="space-y-6 text-sm">
            <div className="flex items-start space-x-3.5">
              <div className="p-2.5 rounded-xl bg-accent text-white shrink-0">
                <MapPin className="w-5 h-5" />
              </div>
              <div>
                <h4 className="font-bold text-xs uppercase tracking-wider text-slate-400">Headquarters</h4>
                <p className="font-semibold text-slate-200 mt-0.5">ACCOMO Plaza, City Center</p>
              </div>
            </div>

            <div className="flex items-start space-x-3.5">
              <div className="p-2.5 rounded-xl bg-accent text-white shrink-0">
                <Mail className="w-5 h-5" />
              </div>
              <div>
                <h4 className="font-bold text-xs uppercase tracking-wider text-slate-400">Email Us</h4>
                <p className="font-semibold text-slate-200 mt-0.5">concierge@accomo.com</p>
              </div>
            </div>

            <div className="flex items-start space-x-3.5">
              <div className="p-2.5 rounded-xl bg-accent text-white shrink-0">
                <Phone className="w-5 h-5" />
              </div>
              <div>
                <h4 className="font-bold text-xs uppercase tracking-wider text-slate-400">Call Desk</h4>
                <p className="font-semibold text-slate-200 mt-0.5">+1 (800) 555-ACCOMO</p>
              </div>
            </div>
          </div>
        </div>

        {/* Contact Form */}
        <div className="lg:col-span-2 bg-white p-8 sm:p-10 rounded-3xl border border-slate-200/80 shadow-lg">
          {submitted ? (
            <div className="py-12 text-center space-y-4">
              <div className="w-16 h-16 rounded-full bg-emerald-100 text-emerald-600 flex items-center justify-center mx-auto shadow-md">
                <CheckCircle2 className="w-10 h-10" />
              </div>
              <h3 className="font-serif text-2xl font-bold text-brand-900">Message Sent!</h3>
              <p className="text-sm text-slate-500 max-w-md mx-auto">
                Thank you for contacting ACCOMO support. Our concierge team will reply to <strong className="text-brand-900">{email}</strong> promptly.
              </p>
              <button
                onClick={() => setSubmitted(false)}
                className="px-6 py-2.5 rounded-xl bg-accent text-white text-xs font-bold shadow-md"
              >
                Send Another Message
              </button>
            </div>
          ) : (
            <form onSubmit={handleSubmit} className="space-y-5">
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                  <label htmlFor="name" className="text-xs font-bold text-slate-500 uppercase tracking-wider block mb-1.5">
                    Your Name
                  </label>
                  <input
                    type="text"
                    id="name"
                    required
                    placeholder="Full Name"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    className="w-full bg-slate-50 border border-slate-200 text-brand-900 text-sm font-medium rounded-xl p-3 focus:ring-2 focus:ring-accent/20 focus:border-accent outline-none"
                  />
                </div>

                <div>
                  <label htmlFor="email" className="text-xs font-bold text-slate-500 uppercase tracking-wider block mb-1.5">
                    Email Address
                  </label>
                  <input
                    type="email"
                    id="email"
                    required
                    placeholder="name@example.com"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    className="w-full bg-slate-50 border border-slate-200 text-brand-900 text-sm font-medium rounded-xl p-3 focus:ring-2 focus:ring-accent/20 focus:border-accent outline-none"
                  />
                </div>
              </div>

              <div>
                <label htmlFor="subject" className="text-xs font-bold text-slate-500 uppercase tracking-wider block mb-1.5">
                  Subject
                </label>
                <input
                  type="text"
                  id="subject"
                  required
                  placeholder="Subject of your inquiry..."
                  value={subject}
                  onChange={(e) => setSubject(e.target.value)}
                  className="w-full bg-slate-50 border border-slate-200 text-brand-900 text-sm font-medium rounded-xl p-3 focus:ring-2 focus:ring-accent/20 focus:border-accent outline-none"
                />
              </div>

              <div>
                <label htmlFor="message" className="text-xs font-bold text-slate-500 uppercase tracking-wider block mb-1.5">
                  Message
                </label>
                <textarea
                  id="message"
                  required
                  rows={5}
                  placeholder="Write your message here..."
                  value={message}
                  onChange={(e) => setMessage(e.target.value)}
                  className="w-full bg-slate-50 border border-slate-200 text-brand-900 text-sm font-medium rounded-xl p-3 focus:ring-2 focus:ring-accent/20 focus:border-accent outline-none"
                />
              </div>

              <button
                type="submit"
                className="w-full py-4 rounded-xl bg-accent hover:bg-accent-hover text-white text-base font-bold shadow-lg shadow-accent/25 transition-all duration-200 flex items-center justify-center gap-2"
              >
                <Send className="w-5 h-5" />
                <span>Send Message</span>
              </button>
            </form>
          )}
        </div>

      </div>
    </div>
  );
};
