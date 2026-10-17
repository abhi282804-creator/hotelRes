import React, { useState, useEffect, useMemo } from 'react';
import { Hotel } from '../types';
import { hotelService } from '../services/hotelService';
import { HeroBanner } from '../components/home/HeroBanner';
import { SearchBar } from '../components/home/SearchBar';
import { LocationFilter } from '../components/home/LocationFilter';
import { CategoryFilter } from '../components/home/CategoryFilter';
import { PriceRangeSlider } from '../components/home/PriceRangeSlider';
import { SortDropdown } from '../components/home/SortDropdown';
import { HotelGrid } from '../components/home/HotelGrid';

export const HomePage: React.FC = () => {
  const [hotels, setHotels] = useState<Hotel[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  // Filter state
  const [searchTerm, setSearchTerm] = useState<string>('');
  const [selectedCategory, setSelectedCategory] = useState<string>('all');
  const [selectedLocation, setSelectedLocation] = useState<string>('all');
  const [minPrice, setMinPrice] = useState<number>(10000);
  const [sortBy, setSortBy] = useState<'price' | 'rating'>('price');

  const fetchHotels = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await hotelService.getHotels();
      setHotels(data);
    } catch (err: any) {
      setError(err.message || 'Failed to load hotels catalog');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchHotels();
  }, []);

  const locations = useMemo(() => {
    const unique = Array.from(new Set(hotels.map((h) => h.location).filter(Boolean)));
    return ['all', ...unique];
  }, [hotels]);

  // Filter & Sort Logic matching index.jsp filterHotels() and sortHotels()
  const filteredAndSortedHotels = useMemo(() => {
    let result = [...hotels];

    // Search term filter
    if (searchTerm.trim() !== '') {
      const term = searchTerm.toLowerCase();
      result = result.filter(
        (h) =>
          h.name.toLowerCase().includes(term) ||
          h.location.toLowerCase().includes(term) ||
          h.category.toLowerCase().includes(term)
      );
    }

    // Category filter
    if (selectedCategory !== 'all') {
      result = result.filter((h) =>
        h.category.toLowerCase().includes(selectedCategory.toLowerCase())
      );
    }

    // Location filter
    if (selectedLocation !== 'all') {
      result = result.filter(
        (h) => h.location.toLowerCase() === selectedLocation.toLowerCase()
      );
    }

    // Price range filter (data-price >= minPrice && data-price <= maxPrice)
    result = result.filter(
      (h) => h.price >= minPrice && h.price <= 50000
    );

    // Sorting filter
    result.sort((a, b) => {
      if (sortBy === 'price') {
        return a.price - b.price;
      } else {
        return b.rating - a.rating;
      }
    });

    return result;
  }, [hotels, searchTerm, selectedCategory, selectedLocation, minPrice, sortBy]);

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      {/* Hero Banner */}
      <HeroBanner />

      {/* Control Bar Section */}
      <div className="mb-10 space-y-4">
        <SearchBar searchTerm={searchTerm} onSearchChange={setSearchTerm} />

        <LocationFilter
          locations={locations}
          selectedLocation={selectedLocation}
          onSelectLocation={setSelectedLocation}
        />

        <div className="grid grid-cols-1 lg:grid-cols-4 gap-4 items-center">
          <div className="lg:col-span-2">
            <CategoryFilter
              selectedCategory={selectedCategory}
              onSelectCategory={setSelectedCategory}
            />
          </div>

          <div>
            <PriceRangeSlider
              minPrice={minPrice}
              maxPrice={50000}
              onMinPriceChange={setMinPrice}
            />
          </div>

          <div>
            <SortDropdown sortBy={sortBy} onSortChange={setSortBy} />
          </div>
        </div>
      </div>

      {/* Header Title & Result Count */}
      <div className="flex items-center justify-between mb-8 pb-4 border-b border-slate-200/80">
        <div>
          <h2 className="font-serif text-3xl font-bold text-brand-900">Choose Your Hotel</h2>
          <p className="text-xs text-slate-500 mt-1">Explore available rooms & luxury amenities</p>
        </div>

        <span className="text-xs font-semibold px-3 py-1.5 rounded-full bg-white border border-slate-200 shadow-sm text-slate-600">
          Showing <span className="font-bold text-accent">{filteredAndSortedHotels.length}</span> Properties
        </span>
      </div>

      {/* Hotel Cards Grid */}
      <HotelGrid
        hotels={filteredAndSortedHotels}
        loading={loading}
        error={error}
        onRetry={fetchHotels}
      />
    </div>
  );
};

export default HomePage;
